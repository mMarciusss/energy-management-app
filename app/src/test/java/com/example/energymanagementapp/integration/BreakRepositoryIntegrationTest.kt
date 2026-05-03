package com.example.energymanagementapp.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BreakRepositoryIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var breakRepository: BreakRepository
    private lateinit var planActivityRepository: PlanActivityRepository

    private var activityId: Int = 0

    @Before
    fun setup() = runBlocking {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        breakRepository = BreakRepository(db.breakDao())
        planActivityRepository = PlanActivityRepository(db.planActivityDao())

        val date = "2025-01-01"
        planActivityRepository.savePlanActivity(date, 1, "A", 3, false, null)

        activityId = planActivityRepository.getPlanActivities(date).first().id
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `saveBreak should insert new break`() = runBlocking {
        breakRepository.saveBreak(activityId, 30, 1000L, 2000L, false)

        val result = breakRepository.getBreak(activityId)

        assertNotNull(result)
        assertEquals(30, result?.durationMinutes)
    }

    @Test
    fun `saveBreak should update existing break`() = runBlocking {
        breakRepository.saveBreak(activityId, 30, 1000L, 2000L, false)

        breakRepository.saveBreak(activityId, 60, 3000L, 4000L, true)

        val result = breakRepository.getBreak(activityId)

        assertEquals(60, result?.durationMinutes)
        assertEquals(true, result?.isCompleted)
    }

    @Test
    fun `getBreakList should return list`() = runBlocking {
        breakRepository.saveBreak(activityId, 30, 1000L, 2000L, false)

        val list = breakRepository.getBreakList(activityId)

        assertEquals(1, list.size)
    }

    @Test
    fun `deleteBreak should remove break`() = runBlocking {
        breakRepository.saveBreak(activityId, 30, 1000L, 2000L, false)

        breakRepository.deleteBreak(activityId)

        val result = breakRepository.getBreak(activityId)

        assertNull(result)
    }

    @Test
    fun `deleteBreaksByDate should remove breaks for that date`() = runBlocking {
        val date = "2025-01-01"

        breakRepository.saveBreak(activityId, 30, 1000L, 2000L, false)

        breakRepository.deleteBreaksByDate(date)

        val result = breakRepository.getBreak(activityId)

        assertNull(result)
    }
}