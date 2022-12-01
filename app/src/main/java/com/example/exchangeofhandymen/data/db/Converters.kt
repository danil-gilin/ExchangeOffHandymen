package com.example.exchangeofhandymen.data.db

import androidx.room.TypeConverter
import com.example.exchangeofhandymen.entity.GeoPosition
import com.example.exchangeofhandymen.entity.Skill
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object Converters {
    @TypeConverter
    fun fromGeoPositionJson(value: String?): GeoPosition? {
        val gson=Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(GeoPosition::class.java)

        return gson.fromJson(value)
    }

    @TypeConverter
    fun geoPositionToJson(geo: GeoPosition?): String? {
        val gson=Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(GeoPosition::class.java)

        return gson.toJson(geo)
    }


    @TypeConverter
    fun fromlistSkillJson(value: String?): List<Skill>? {
        val type = Types.newParameterizedType(List::class.java, Skill::class.java)
        val gson=Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<Skill>>(type)

        return gson.fromJson(value)
    }

    @TypeConverter
    fun listSkillToJson(skills: List<Skill>?): String? {
        val type = Types.newParameterizedType(List::class.java, Skill::class.java)
        val gson=Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<Skill>>(type)

        return gson.toJson(skills)
    }


    @TypeConverter
    fun fromlistStringJson(value: String?): List<String>? {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val gson=Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<String>>(type)

        return gson.fromJson(value)
    }

    @TypeConverter
    fun listStringToJson(skills: List<String>?): String? {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val gson=Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<String>>(type)

        return gson.toJson(skills)
    }

}