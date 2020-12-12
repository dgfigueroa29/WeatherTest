package com.boa.data.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.boa.data.entity.CityEntity

@Dao
interface CityDao {
    @Update
    fun update(entity: CityEntity)

    @Query("SELECT * FROM cities ORDER BY name")
    fun getAll(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE name LIKE :text ORDER BY name")
    fun getByText(text: String): List<CityEntity>

    @Query("SELECT * FROM cities WHERE selected = 1 ORDER BY name")
    fun getSelected(): List<CityEntity>
}