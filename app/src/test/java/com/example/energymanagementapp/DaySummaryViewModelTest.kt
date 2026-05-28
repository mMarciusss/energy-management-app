package com.example.energymanagementapp

import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.viewmodel.DaySummaryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DaySummaryViewModelTest {

    private lateinit var viewModel: DaySummaryViewModel
    private val repository: PlanActivityRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSummary should load activities`() = runTest {
        val list = listOf(
            PlanActivityWithBreak(
                id = 1,
                planDate = "2025-01-01",
                activityId = 1,
                isCompleted = true,
                completionTime = null,
                activityName = "A",
                energyCost = 3,
                breakDuration = null,
                startTime = null,
                endTime = null,
                breakIsCompleted = null
            ),

            PlanActivityWithBreak(
                id = 2,
                planDate = "2025-01-01",
                activityId = 2,
                isCompleted = false,
                completionTime = null,
                activityName = "B",
                energyCost = 2,
                breakDuration = null,
                startTime = null,
                endTime = null,
                breakIsCompleted = null
            )
        )

        coEvery { repository.getPlanActivitiesWithBreaks(any()) } returns list

        viewModel = DaySummaryViewModel(repository)

        viewModel.loadSummary("2025-01-01")

        advanceUntilIdle()

        assertEquals(2, viewModel.activities.size)
    }

    @Test
    fun `loadSummary should calculate total energy`() = runTest {
        val list = listOf(
            PlanActivityWithBreak(
                id = 1,
                planDate = "2025-01-01",
                activityId = 1,
                isCompleted = true,
                completionTime = null,
                activityName = "A",
                energyCost = 3,
                breakDuration = null,
                startTime = null,
                endTime = null,
                breakIsCompleted = null
            ),

            PlanActivityWithBreak(
                id = 2,
                planDate = "2025-01-01",
                activityId = 2,
                isCompleted = false,
                completionTime = null,
                activityName = "B",
                energyCost = 2,
                breakDuration = null,
                startTime = null,
                endTime = null,
                breakIsCompleted = null
            )
        )

        coEvery { repository.getPlanActivitiesWithBreaks(any()) } returns list

        viewModel = DaySummaryViewModel(repository)

        viewModel.loadSummary("2025-01-01")

        advanceUntilIdle()

        assertEquals(3, viewModel.totalEnergyUsed)
    }

    @Test
    fun `loadSummary should calculate rest time`() = runTest {
        val list = listOf(
            PlanActivityWithBreak(
                id = 1,
                planDate = "2025-01-01",
                activityId = 1,
                isCompleted = true,
                completionTime = null,
                activityName = "A",
                energyCost = 3,
                breakDuration = null,
                startTime = 0L,
                endTime = 0L,
                breakIsCompleted = null
            ),
            PlanActivityWithBreak(
                id = 2,
                planDate = "2025-01-01",
                activityId = 2,
                isCompleted = true,
                completionTime = null,
                activityName = "B",
                energyCost = 2,
                breakDuration = null,
                startTime = 1000L,
                endTime = 61000L,
                breakIsCompleted = null
            )
        )

        coEvery { repository.getPlanActivitiesWithBreaks(any()) } returns list

        viewModel = DaySummaryViewModel(repository)

        viewModel.loadSummary("2025-01-01")

        advanceUntilIdle()

        assertEquals(1, viewModel.totalRestTimeMinutes)
    }
}