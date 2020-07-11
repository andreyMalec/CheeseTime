package com.malec.cheesetime.repo

import com.malec.cheesetime.model.Cheese
import com.malec.cheesetime.service.localDb.CheeseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*

class CheeseRepo(/*private val api: CheeseApi,*/ private val dao: CheeseDao) {
    fun getAll(): Flow<List<Cheese>> = dao.getAll()

    fun getById(id: Long): Flow<Cheese> = dao.getFlowById(id)

    suspend fun create(
        name: String,
        date: Long,
        recipe: String,
        comment: String?,
        milk: String,
        composition: String?,
        stages: String?,
        badgeColor: Int?
    ) = withContext(Dispatchers.IO) {
        val time = Calendar.getInstance().timeInMillis

        val newCheese = Cheese(
            time,
            name,
            date,
            recipe,
            comment ?: "",
            milk,
            composition ?: "",
            stages ?: "",
            badgeColor ?: 0
        )
        dao.insert(newCheese)
    }

    suspend fun update(
        name: String,
        date: Long,
        recipe: String,
        comment: String?,
        milk: String,
        composition: String?,
        stages: String?,
        badgeColor: Int?,
        cheese: Cheese
    ) = withContext(Dispatchers.IO) {
        val updatedCheese = Cheese(
            cheese.id,
            name,
            date,
            recipe,
            comment ?: "",
            milk,
            composition ?: "",
            stages ?: "",
            badgeColor ?: 0
        )
        dao.update(updatedCheese)
    }

    suspend fun deleteById(id: Long) = withContext(Dispatchers.IO) {
        dao.deleteById(id)
    }
}