package com.example.rickandmorty

import com.example.rickandmorty.service.CharacterAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object CreateRetrofitService {
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
    private const val BASE_URL = "https://rickandmortyapi.com/api/"
    private val refrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: CharacterAPI = refrofit.create(CharacterAPI::class.java)

}