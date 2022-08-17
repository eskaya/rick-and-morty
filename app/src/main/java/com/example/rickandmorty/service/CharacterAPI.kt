package com.example.rickandmorty.service

import com.example.rickandmorty.model.CharacterDetailModel
import com.example.rickandmorty.model.CharacterModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterAPI {

    @GET("character")
    fun getCharacter(
        @Query("status") status: String,
        @Query("gender") gender: String
    ): Call<CharacterModel>

    @GET("character/{character_id}")
    fun getCharacterDetail(@Path("character_id") character_id: String): Call<CharacterDetailModel>
}