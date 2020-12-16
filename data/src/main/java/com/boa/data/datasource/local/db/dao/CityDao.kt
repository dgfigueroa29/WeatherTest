package com.boa.data.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boa.data.entity.CityEntity

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CityEntity)

    @Query("SELECT * FROM cities ORDER BY selected, name")
    fun getAll(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE LOWER(name) LIKE :text ORDER BY selected, name")
    fun getByText(text: String): List<CityEntity>

    @Query("SELECT * FROM cities WHERE selected = 1 ORDER BY selected, name")
    fun getSelected(): List<CityEntity>
}