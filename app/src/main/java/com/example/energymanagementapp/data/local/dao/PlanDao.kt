package com.example.energymanagementapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.energymanagementapp.data.local.entities.PlanEntity

@Dao
interface PlanDao {
    // įrašomas arba atnaujinamas plano įrašas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlan(plan: PlanEntity)

    // ištrinamas plano įrašas
    @Delete
    suspend fun deletePlan(plan: PlanEntity)

    // plano nuskaitymas pagal datą
    @Query("SELECT * FROM plans WHERE date = :date LIMIT 1")
    suspend fun getPlanByDate(date: String): PlanEntity?

    // plano patvirtinimas
    @Query("Update plans SET isConfirmed = 1 WHERE date = :date")
    suspend fun confirmPlan(date: String)
}