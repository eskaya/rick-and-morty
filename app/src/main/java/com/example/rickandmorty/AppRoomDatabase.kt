package com.example.rickandmorty

import android.content.Context
import androidx.room.*
import com.example.rickandmorty.model.RoomCharacterGroupModel
import com.example.rickandmorty.model.RoomCharacterModel
import com.example.rickandmorty.service.CharacterDao

@Database(entities = [RoomCharacterModel::class,RoomCharacterGroupModel::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun roomCharacterDao(): CharacterDao
}

object DatabaseBuilder {
    private var INSTANCE: AppRoomDatabase? = null

    fun getInstance(context: Context): AppRoomDatabase {
        val tempInstance = INSTANCE
        if (tempInstance != null) {
            return tempInstance
        }
        synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java,
                "app_room_database"
            ).build()
            INSTANCE = instance
            return instance
        }
    }
}
