package com.example.energymanagementapp.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.PlanRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlanRepositoryIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: PlanRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = PlanRepository(db.planDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `saveEnergy and getPlan should work correctly`() = runBlocking {
        val date = "2025-01-01"

        repository.saveEnergy(date, 10)

        val plan = repository.getPlan(date)

        assertNotNull(plan)
        assertEquals(10, plan?.energyLevel)
    }

    @Test
    fun `confirmPlan should set isConfirmed to true`() = runBlocking {
        val date = "2025-01-01"

        repository.saveEnergy(date, 10)
        repository.confirmPlan(date)

        val plan = repository.getPlan(date)

        assertEquals(true, plan?.isConfirmed)
    }

    @Test
    fun `deletePlan should remove plan`() = runBlocking {
        val date = "2025-01-01"

        repository.saveEnergy(date, 10)
        repository.deletePlan(date)

        val plan = repository.getPlan(date)

        assertNull(plan)
    }

    @Test
    fun `updateEndTime should update correctly`() = runBlocking {
        val date = "2025-01-01"

        repository.saveEnergy(date, 10)
        repository.updateEndTime(date, "22:00")

        val plan = repository.getPlan(date)

        assertEquals("22:00", plan?.endTime)
    }
}