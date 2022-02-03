package com.malec.domain.repository

import com.malec.domain.api.CheeseApi
import com.malec.domain.api.StorageApi
import com.malec.domain.model.Cheese
import com.malec.domain.model.CheeseFilter
import com.malec.domain.model.Photo
import com.malec.domain.model.PhotoF
import com.malec.domain.util.CheeseSharer

class CheeseRepo(
    private val api: CheeseApi,
    private val storageApi: StorageApi,
    private val sharer: CheeseSharer
) {
    private val selected = mutableListOf<Long>()

    suspend fun getNextId() = try {
        api.getNextId()
    } catch (e: Exception) {
        e.printStackTrace()
        1L
    }

    suspend fun getAllSelected() = api.getAll().filter {
        selected.contains(it.id)
    }

    suspend fun getAllFiltered(filter: CheeseFilter): List<Cheese> {
        val cheeses = api.getAllFiltered(filter)
        cheeses.forEach { cheese ->
            if (selected.contains(cheese.id))
                cheese.toggleSelect()
        }
        return cheeses
    }

    suspend fun getAllTitleContains(filter: CheeseFilter, searchQuery: String) =
        getAllFiltered(filter).filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

    suspend fun getById(id: Long) = api.getById(id)

    suspend fun create(cheese: Cheese) = api.create(cheese)

    suspend fun update(cheese: Cheese) {
        val updated = if (cheese.volume == "0")
            cheese.archive()
        else
            cheese
        api.update(updated)
    }

    suspend fun delete(cheese: Cheese) = api.update(cheese.delete())

    fun share(cheese: Cheese) = sharer.send(cheese)

    fun share(cheeseList: List<Cheese>) = sharer.send(cheeseList)

    suspend fun deletePhotos(photos: List<Photo>?) {
        photos?.forEach {
            storageApi.delete(it)
        }
    }

    suspend fun updatePhotos(oldPhotos: List<String>?, new: List<Photo>?) {
        val newPhotos = new?.map { it.name }

        if (oldPhotos != null && oldPhotos.isNotEmpty() && oldPhotos[0].isNotBlank()) {
            if (newPhotos != null && newPhotos.isNotEmpty() && newPhotos[0].isNotBlank()) {
                val diff1 = (oldPhotos - newPhotos).toSet()
                val diff2 = (newPhotos - oldPhotos).toSet()

                for (oldPhoto in diff1)
                    storageApi.deleteById(oldPhoto)

                for (newPhoto in new.filter { diff2.contains(it.name) })
                    storageApi.save(newPhoto)

                return
            }
        }

        new?.let {
            for (newPhoto in it)
                storageApi.save(newPhoto)
        }
    }

    fun getPhotosById(photosNames: List<String>) =
        photosNames.filter { it.isNotBlank() }.map {
            Photo(
                it,
                null,
                storageApi.getRefById(it)
            )
        }

    fun convertPhoto(photo: PhotoF): Photo {
        val ref = photo.ref?.let { storageApi.getRefById(photo.name) }
        return Photo(photo.name, photo.content, ref)
    }
}