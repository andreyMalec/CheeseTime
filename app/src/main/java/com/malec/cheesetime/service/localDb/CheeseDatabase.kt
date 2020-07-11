package com.malec.cheesetime.service.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.malec.cheesetime.model.Cheese

@Database(entities = [Cheese::class], version = 1)
abstract class CheeseDatabase: RoomDatabase() {
    companion object {
        fun instance(context: Context): CheeseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CheeseDatabase::class.java, "cheeseDb"
            ).build()
        }
    }

    abstract fun cheeseDataDao(): CheeseDao
}