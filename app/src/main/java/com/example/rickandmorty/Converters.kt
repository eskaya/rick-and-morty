package com.example.rickandmorty

import androidx.room.TypeConverter
import com.example.rickandmorty.model.LocationModel
import com.example.rickandmorty.model.OriginModel
import com.example.rickandmorty.model.Result
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken


object Converters {
    @TypeConverter
    fun fromResultList(list: ArrayList<Result?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Result?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toResultList(countryLangString: String?): ArrayList<Result>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Result?>?>() {}.type
        return gson.fromJson<ArrayList<Result>>(countryLangString, type)
    }
}