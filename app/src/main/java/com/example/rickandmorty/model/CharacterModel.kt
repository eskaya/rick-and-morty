package com.example.rickandmorty.model

import kotlinx.serialization.Serializable

data class CharacterModel(
    val info: Info,
    val results: ArrayList<Result>
)

@Serializable
 data class Result (
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val origin: OriginModel?,
    val location: LocationModel?,
    val image: String
): java.io.Serializable

fun Result.toRoomCharacterModel() = RoomCharacterModel(name, status, species, origin?.name, location?.name, image, id)

@Serializable
data class LocationModel(
    val name: String,
    val url: String
): java.io.Serializable

@Serializable
data class OriginModel(
    var name: String,
    val url: String
): java.io.Serializable

data class Info(
    val count: String,
    val pages: String,
    val next: String,
    val prev: String,
)