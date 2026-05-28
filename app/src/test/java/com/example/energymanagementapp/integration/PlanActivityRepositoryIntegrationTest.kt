package com.example.energymanagementapp.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlanActivityRepositoryIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: PlanActivityRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = PlanActivityRepository(db.planActivityDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `savePlanActivity and getPlanActivities should work`() = runBlocking {
        val date = "2025-01-01"

        repository.savePlanActivity(date, 1, "A", 3, false, null)

        val list = repository.getPlanActivities(date)

        assertEquals(1, list.size)
        assertEquals("A", list.first().activityName)
    }

    @Test
    fun `getAllDates should return inserted dates`() = runBlocking {
        repository.savePlanActivity("2025-01-01", 1, "A", 3, false, null)
        repository.savePlanActivity("2025-01-02", 2, "B", 2, false, null)

        val dates = repository.getAllDates()

        assertEquals(2, dates.size)
    }

    @Test
    fun `deletePlanActivitiesByDate should remove all activities for date`() = runBlocking {
        val date = "2025-01-01"

        repository.savePlanActivity(date, 1, "A", 3, false, null)
        repository.deletePlanActivitiesByDate(date)

        val list = repository.getPlanActivities(date)

        assertTrue(list.isEmpty())
    }

    @Test
    fun `deletePlanActivityByDateAndActivityId should remove specific activity`() = runBlocking {
        val date = "2025-01-01"

        repository.savePlanActivity(date, 1, "A", 3, false, null)
        repository.savePlanActivity(date, 2, "B", 2, false, null)

        repository.deletePlanActivityByDateAndActivityId(date, 1)

        val list = repository.getPlanActivities(date)

        assertEquals(1, list.size)
        assertEquals(2, list.first().activityId)
    }

    @Test
    fun `completeActivity should update status`() = runBlocking {
        val date = "2025-01-01"

        repository.savePlanActivity(date, 1, "A", 3, false, null)

        val activity = repository.getPlanActivities(date).first()

        repository.completeActivity(activity.id, "12:00")

        val updated = repository.getPlanActivities(date).first()

        assertTrue(updated.isCompleted)
        assertEquals("12:00", updated.completionTime)
    }

    @Test
    fun `getPlanActivitiesWithBreaks should return data without breaks`() = runBlocking {
        val date = "2025-01-01"

        repository.savePlanActivity(date, 1, "A", 3, false, null)

        val list = repository.getPlanActivitiesWithBreaks(date)

        assertEquals(1, list.size)
        assertEquals("A", list.first().activityName)
    }
}