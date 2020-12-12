package com.boa.data.entity

import androidx.room.Embedded
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
    val state: String = "",
    val country: String = "",
    @Embedded
    val coord: Coord,
    val selected: Boolean = false
)

/**
 * Coordinates
 */
data class Coord(val lat: Double = 0.0, val lon: Double = 0.0)