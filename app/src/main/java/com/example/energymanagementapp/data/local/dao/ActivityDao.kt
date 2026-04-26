package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.ActivityEntity

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(activity: ActivityEntity)

    @Delete("DELETE FROM activities WHERE id = :id")
    suspend fun deleteActivity(id: Int)

    @Query("SELECT * FROM activities")
    suspend fun getActivities(): List<ActivityEntity>
}