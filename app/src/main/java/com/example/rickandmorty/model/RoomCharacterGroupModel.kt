package com.example.rickandmorty.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "room_character_group_table")
data class RoomCharacterGroupModel(
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val itemNumber: String,
    @ColumnInfo val groupItem: ArrayList<Result>,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): java.io.Serializable

