package com.example.rickandmorty.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_character_table")
data class RoomCharacterModel(
    @ColumnInfo val name: String,
    @ColumnInfo val status: String?,
    @ColumnInfo val species: String?,
    @ColumnInfo val origin: String?,
    @ColumnInfo val location: String?,
    @ColumnInfo val image: String,
    @PrimaryKey val id: String,
)

fun RoomCharacterModel.toResult(): Result {
    return Result(
        id = id,
        name = name,
        status = status ?: "",
        species = species ?: "",
        origin = OriginModel(origin ?: "", ""),
        location = LocationModel(location ?: "", ""),
        image = image
    )
}
