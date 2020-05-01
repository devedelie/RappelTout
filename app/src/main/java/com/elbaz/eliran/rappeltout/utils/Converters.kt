package com.elbaz.eliran.rappeltout.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by Eliran Elbaz on 30-Apr-20.
 */
object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType = object :
            TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToMapStringString(value: String?): Map<String, String>? {
        if (value == null) {
            return null
        }
        val mapType = object :
            TypeToken<Map<String?, String?>?>() {}.type
        return Gson()
            .fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMapStringStringToString(map: Map<String?, String?>?): String? {
        if (map == null) {
            return null
        }
        val gson = Gson()
        return gson.toJson(map)
    }
}
