package com.boa.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * City entity
 */
@Entity(
    tableName = "cities",
    indices = [
        Index(name = "city_name", value = ["name"])
    ]
)
data class CityEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val state: String? = "",
    val country: String = "",
    @ColumnInfo(name = "lat") val latitude: Double = 0.0,
    @ColumnInfo(name = "lon") val longitude: Double = 0.0,
    val selected: Boolean? = false
)