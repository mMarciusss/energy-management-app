package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.ActivityEntity

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(activity: ActivityEntity)

    @Query("SELECT * FROM activities")
    suspend fun getActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE name = :name LIMIT 1")
    suspend fun getActivityByName(name: String): ActivityEntity?
}