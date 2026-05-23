package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.ActivityEntity

@Dao
interface ActivityDao {
    // įrašomas arba atnaujinamas veiklos įrašas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(activity: ActivityEntity)

    // veiklos ištrynimas
    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    // veiklų nuskaitymas iš DB
    @Query("SELECT * FROM activities")
    suspend fun getActivities(): List<ActivityEntity>
}