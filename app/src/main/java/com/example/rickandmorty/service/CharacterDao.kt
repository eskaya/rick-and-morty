package com.example.rickandmorty.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rickandmorty.model.RoomCharacterGroupModel
import com.example.rickandmorty.model.RoomCharacterModel

@Dao
interface CharacterDao {

    @Insert
    fun insert(character: RoomCharacterModel)

    @Query("SELECT * FROM room_character_table")
    suspend fun getAllCharacter(): List<RoomCharacterModel>

    @Query("SELECT * FROM room_character_table WHERE id LIKE :id")
    suspend fun getCharacter(id: String): RoomCharacterModel

    @Delete
    suspend fun delete(character: RoomCharacterModel)

    @Insert
    suspend fun insertCharacterGroup(character: RoomCharacterGroupModel)

    @Query("SELECT * FROM room_character_group_table")
    fun getAllCharacterGroup(): MutableList<RoomCharacterGroupModel>
}