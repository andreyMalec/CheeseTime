package com.malec.cheesetime.repo

import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.model.Photo
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.service.network.StorageApi
import com.malec.cheesetime.util.CheeseSharer

class CheeseRepo(
    private val api: CheeseApi,
    private val storageApi: StorageApi,
    private val sharer: CheeseSharer
) {
    private val selected = mutableListOf<Long>()

    suspend fun getNextId() = api.getNextId()

    suspend fun getAll() = api.getAll()

    suspend fun getAllSelected() = api.getAll().filter {
        selected.contains(it.id)
    }

    suspend fun getAllFiltered(filter: CheeseFilter) =
        api.getAllFiltered(filter).map {
            if (selected.contains(it.id))
                it.toggleSelect()
            else
                it
        }

    suspend fun getById(id: Long) = api.getById(id)

    suspend fun create(cheese: Cheese) = api.create(cheese)

    suspend fun update(cheese: Cheese) = api.update(cheese)

    suspend fun deleteById(id: Long) = api.deleteById(id)

    fun share(cheese: Cheese) = sharer.send(cheese)

    fun share(cheeseList: List<Cheese>) = sharer.send(cheeseList)

    fun toggleSelect(cheese: Cheese) {
        if (!cheese.isSelected)
            selected.add(cheese.id)
        else
            selected.remove(cheese.id)
    }

    fun unselect() {
        selected.clear()
    }

    fun getSelectedIds() = selected.toList()

    suspend fun archiveSelected() {
        selected.forEach { id ->
            getById(id)?.toggleArchive()?.let {
                update(it)
            }
        }
        selected.clear()
    }

    suspend fun deleteSelected() {
        selected.forEach {
            deleteById(it)
        }
        selected.clear()
    }

    suspend fun deletePhotos(photos: List<Photo>?) {
        photos?.forEach {
            storageApi.delete(it)
        }
    }

    suspend fun updatePhotos(old: String?, new: List<Photo>?) {
        val oldPhotos = old?.split("♂")
        val newPhotos = new?.map { it.name }

        if (oldPhotos != null && oldPhotos.isNotEmpty() && oldPhotos[0].isNotBlank()) {
            if (newPhotos != null && newPhotos.isNotEmpty() && newPhotos[0].isNotBlank()) {
                val diff1 = oldPhotos.toSet() - newPhotos.toSet()
                val diff2 = newPhotos.toSet() - oldPhotos.toSet()

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

    fun getPhotosById(formattedId: String) =
        formattedId.split("♂").filter { it.isNotBlank() }.map {
            Photo(
                it,
                null,
                storageApi.getUriById(it)
            )
        }
}