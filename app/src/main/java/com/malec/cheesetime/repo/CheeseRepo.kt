package com.malec.cheesetime.repo

import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.model.CheeseFilter
import com.malec.cheesetime.service.network.CheeseApi
import com.malec.cheesetime.util.CheeseSharer
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CheeseRepo(private val api: CheeseApi, private val sharer: CheeseSharer) {
    private val selected = mutableListOf<Long>()

    suspend fun getNextId() = api.getNextId()

    suspend fun getAll() = api.getAll()

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

    fun toggleSelect(cheese: Cheese) {
        if (!cheese.isSelected)
            selected.add(cheese.id)
        else
            selected.remove(cheese.id)
    }

    fun getSelected() = selected.toList()
}