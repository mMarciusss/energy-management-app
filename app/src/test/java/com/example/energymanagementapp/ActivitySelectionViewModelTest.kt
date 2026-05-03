package com.example.energymanagementapp

import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.local.entities.PlanActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.viewmodel.ActivitySelectionModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
class ActivitySelectionViewModelTest {

    private lateinit var viewModel: ActivitySelectionModel

    private val activityRepository: ActivityRepository = mockk(relaxed = true)
    private val planActivityRepository: PlanActivityRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `initEnergy should set initial and remaining energy`() {
        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)

        viewModel.initEnergy(10)

        assertEquals(10, viewModel.initialEnergy)
        assertEquals(10, viewModel.remainingEnergy)
        assertTrue(viewModel.isIntialized)
    }

    @Test
    fun `toggleActivity should add activity and decrease energy`() {
        val activity = ActivityEntity(id = 1, name = "Test", energyCost = 3)

        coEvery { activityRepository.getActivityList() } returns listOf(activity)

        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        viewModel.initEnergy(10)

        viewModel.toggleActivity(activity)

        assertTrue(viewModel.selectedActivities.contains(1))
        assertEquals(7, viewModel.remainingEnergy)
    }

    @Test
    fun `toggleActivity should remove activity and increase energy`() {
        val activity = ActivityEntity(id = 1, name = "Test", energyCost = 3)

        coEvery { activityRepository.getActivityList() } returns listOf(activity)

        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        viewModel.initEnergy(10)

        viewModel.toggleActivity(activity)
        viewModel.toggleActivity(activity)

        assertFalse(viewModel.selectedActivities.contains(1))
        assertEquals(10, viewModel.remainingEnergy)
    }

    @Test
    fun `toggleActivity should not add activity if not enough energy`() {
        val activity = ActivityEntity(id = 1, name = "Test", energyCost = 10)

        coEvery { activityRepository.getActivityList() } returns listOf(activity)

        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        viewModel.initEnergy(5)

        viewModel.toggleActivity(activity)

        assertFalse(viewModel.selectedActivities.contains(1))
        assertEquals(5, viewModel.remainingEnergy)
    }

    @Test
    fun `getTotalSelectedEnergy should return correct sum`() = runTest {
        val a1 = ActivityEntity(1, "A", 3)
        val a2 = ActivityEntity(2, "B", 2)

        coEvery { activityRepository.getActivityList() } returns listOf(a1, a2)

        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)

        advanceUntilIdle()

        viewModel.initEnergy(10)

        viewModel.toggleActivity(a1)
        viewModel.toggleActivity(a2)

        val total = viewModel.getTotalSelectedEnergy()

        assertEquals(5, total)
    }

    @Test
    fun `savePlanActivities should call repository`() = runTest {
        val activity = ActivityEntity(1, "A", 3)

        coEvery { activityRepository.getActivityList() } returns listOf(activity)
        coEvery { planActivityRepository.getPlanActivities(any()) } returns emptyList()
        coEvery { planActivityRepository.savePlanActivity(any(), any(), any(), any(), any(), any()) } just Runs

        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)

        advanceUntilIdle()

        viewModel.initEnergy(10)

        advanceUntilIdle()

        viewModel.toggleActivity(activity)

        var callbackCalled = false

        viewModel.savePlanActivities {
            callbackCalled = true
        }

        advanceUntilIdle()

        coVerify {
            planActivityRepository.savePlanActivity(
                any(), 1, "A", 3, false, null
            )
        }

        assertTrue(callbackCalled)
    }

    @Test
    fun `loadSelectedActivities should restore selected and recalc energy`() = runTest {
        val activity = ActivityEntity(1, "A", 3)

        coEvery { planActivityRepository.getPlanActivities(any()) } returns listOf(
            PlanActivityEntity(
                id = 1,
                planDate = "2025-01-01",
                activityId = 1,
                activityName = "A",
                energyCost = 3,
                isCompleted = false,
                completionTime = null
            )
        )

        coEvery { activityRepository.getActivityList() } returns listOf(activity)

        viewModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        viewModel.initEnergy(10)

        advanceUntilIdle()

        assertTrue(viewModel.selectedActivities.contains(1))
        assertEquals(7, viewModel.remainingEnergy)
    }
}