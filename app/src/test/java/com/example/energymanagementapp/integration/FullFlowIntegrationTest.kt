package com.example.energymanagementapp.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.*
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FullFlowIntegrationTest {

    private lateinit var db: AppDatabase

    private lateinit var planRepository: PlanRepository
    private lateinit var activityRepository: ActivityRepository
    private lateinit var planActivityRepository: PlanActivityRepository
    private lateinit var breakRepository: BreakRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        planRepository = PlanRepository(db.planDao())
        activityRepository = ActivityRepository(db.activityDao())
        planActivityRepository = PlanActivityRepository(db.planActivityDao())
        breakRepository = BreakRepository(db.breakDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `full flow should work correctly`() = runBlocking {

        val date = "2025-01-01"

        // 1. sukuriam planą
        planRepository.saveEnergy(date, 10)

        val plan = planRepository.getPlan(date)
        assertNotNull(plan)
        assertEquals(10, plan?.energyLevel)

        // 2. sukuriam veiklą
        activityRepository.saveActivity("Workout", 3)
        val activity = activityRepository.getActivityList().first()

        // 3. pridedam veiklą į planą
        planActivityRepository.savePlanActivity(
            planDate = date,
            activityId = activity.id,
            activityName = activity.name,
            energyCost = activity.energyCost,
            isCompleted = false,
            completionTime = null
        )

        val planActivities = planActivityRepository.getPlanActivities(date)
        assertEquals(1, planActivities.size)

        val planActivityId = planActivities.first().id

        // 4. pridedam break
        breakRepository.saveBreak(
            planActivityId = planActivityId,
            durationMinutes = 30,
            startTime = 1000L,
            endTime = 2000L,
            isCompleted = false
        )

        val breakData = breakRepository.getBreak(planActivityId)
        assertNotNull(breakData)
        assertEquals(30, breakData?.durationMinutes)

        // 5. pažymim veiklą kaip completed
        planActivityRepository.completeActivity(planActivityId, "12:00")

        val updatedActivity = planActivityRepository.getPlanActivities(date).first()
        assertTrue(updatedActivity.isCompleted)

        // 6. tikrinam JOIN rezultatą
        val fullData = planActivityRepository.getPlanActivitiesWithBreaks(date)

        assertEquals(1, fullData.size)

        val item = fullData.first()

        assertEquals("Workout", item.activityName)
        assertEquals(30, item.breakDuration)
        assertTrue(item.isCompleted)
    }
}