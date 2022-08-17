package com.example.rickandmorty.model

data class CharacterDetailModel(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val origin: OriginModel,
    val location: LocationModel,
    val image: String
)

fun CharacterDetailModel.toRoomCharacterModel() =
     RoomCharacterModel(
         name, status, species, origin.name, location.name, image, id
    )

