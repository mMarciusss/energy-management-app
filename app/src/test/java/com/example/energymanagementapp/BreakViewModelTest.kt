package com.example.energymanagementapp

import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.viewmodel.BreakViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class BreakViewModelTest {
    private lateinit var viewModel: BreakViewModel

    private val planActivityRepository: PlanActivityRepository = mockk(relaxed = true)
    private val breakRepository: BreakRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `increaseBreakDuration should increase by 5`() {
        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        viewModel.increaseBreakDuration()

        assertEquals(35, viewModel.breakDuration)
    }

    @Test
    fun `increaseBreakDuration should not exceed 180`() {
        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        repeat(40) { viewModel.increaseBreakDuration() }

        assertEquals(180, viewModel.breakDuration)
    }

    @Test
    fun `decreaseBreakDuration should not go below 5`() {
        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        repeat(10) { viewModel.decreaseBreakDuration() }

        assertEquals(5, viewModel.breakDuration)
    }

    @Test
    fun `createBreak should call repository`() = runTest {
        coEvery { breakRepository.saveBreak(any(), any(), any(), any(), any()) } just Runs

        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        viewModel.createBreak(1)

        advanceUntilIdle()

        coVerify {
            breakRepository.saveBreak(1, viewModel.breakDuration, any(), any(), false)
        }
    }

    @Test
    fun `removeBreak should delete and reload`() = runTest {
        coEvery { breakRepository.deleteBreak(any()) } just Runs
        coEvery { planActivityRepository.getPlanActivitiesWithBreaks(any()) } returns emptyList()

        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        viewModel.removeBreak(1)

        advanceUntilIdle()

        coVerify { breakRepository.deleteBreak(1) }
    }

    @Test
    fun `loadBreak should set hasBreak true when exists`() = runTest {
        coEvery { breakRepository.getBreak(any()) } returns mockk {
            every { durationMinutes } returns 45
        }

        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        viewModel.loadBreak(1)

        advanceUntilIdle()

        assertTrue(viewModel.hasBreak)
        assertEquals(45, viewModel.breakDuration)
    }

    @Test
    fun `loadBreak should reset when no break`() = runTest {
        coEvery { breakRepository.getBreak(any()) } returns null

        viewModel = BreakViewModel(planActivityRepository, breakRepository)

        viewModel.loadBreak(1)

        advanceUntilIdle()

        assertFalse(viewModel.hasBreak)
        assertEquals(30, viewModel.breakDuration)
    }

    @Test
    fun `completeActivities should complete without break`() = runTest {
        val activity = mockk<PlanActivityWithBreak> {
            every { id } returns 1
            every { breakDuration } returns null
            every { energyCost } returns 3
            every { isCompleted } returns false
        }

        coEvery { planActivityRepository.getPlanActivitiesWithBreaks(any()) } returns listOf(activity)
        coEvery { planActivityRepository.completeActivity(any(), any()) } just Runs

        viewModel = BreakViewModel(planActivityRepository, breakRepository)
        viewModel.planActivities = listOf(activity)
        viewModel.setEnergy(10)

        var breakCalled: Int? = -1

        viewModel.completeActivities(listOf(1)) {
            breakCalled = it
        }

        advanceUntilIdle()

        assertEquals(null, breakCalled)
    }

    @Test
    fun `completeActivities should trigger break`() = runTest {
        val activity = mockk<PlanActivityWithBreak> {
            every { id } returns 1
            every { breakDuration } returns 30
            every { energyCost } returns 3
            every { isCompleted } returns false
        }

        coEvery { planActivityRepository.getPlanActivitiesWithBreaks(any()) } returns listOf(activity)

        viewModel = BreakViewModel(planActivityRepository, breakRepository)
        viewModel.planActivities = listOf(activity)

        var breakId: Int? = null

        viewModel.completeActivities(listOf(1)) {
            breakId = it
        }

        advanceUntilIdle()

        assertEquals(1, breakId)
    }

    @Test
    fun `getRunningBreakActivityId should return active break`() {
        val activity = mockk<PlanActivityWithBreak> {
            every { id } returns 1
            every { startTime } returns 100L
            every { breakIsCompleted } returns false
            every { isCompleted } returns false
        }

        viewModel = BreakViewModel(planActivityRepository, breakRepository)
        viewModel.planActivities = listOf(activity)

        val result = viewModel.getRunningBreakActivityId()

        assertEquals(1, result)
    }

    @Test
    fun `areAllActivitiesCompleted should return true`() {
        val activity = mockk<PlanActivityWithBreak> {
            every { isCompleted } returns true
        }

        viewModel = BreakViewModel(planActivityRepository, breakRepository)
        viewModel.planActivities = listOf(activity)

        assertTrue(viewModel.areAllActivitiesCompleted())
    }
}