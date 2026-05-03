package com.example.energymanagementapp

import com.example.energymanagementapp.core.state.PlanState
import com.example.energymanagementapp.data.local.entities.PlanEntity
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.viewmodel.PlanViewModel
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
class PlanViewModelTest {

    private lateinit var viewModel: PlanViewModel

    private val planRepository: PlanRepository = mockk(relaxed = true)
    private val breakRepository: BreakRepository = mockk(relaxed = true)
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
    fun `startCreatingPlan should change state to CREATING`() {
        viewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)

        viewModel.startCreatingPlan()

        assertEquals(PlanState.CREATING, viewModel.planState)
    }

    @Test
    fun `confirmPlan should update state and call repository`() = runTest {
        coEvery { planRepository.confirmPlan(any()) } just Runs

        viewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)

        var callbackCalled = false

        viewModel.confirmPlan {
            callbackCalled = true
        }

        advanceUntilIdle()

        coVerify { planRepository.confirmPlan(any()) }
        assertTrue(viewModel.isConfirmed)
        assertEquals(PlanState.CONFIRMED, viewModel.planState)
        assertTrue(callbackCalled)
    }

    @Test
    fun `resetPlan should delete all data and reset state`() = runTest {
        coEvery { planRepository.deletePlan(any()) } just Runs
        coEvery { breakRepository.deleteBreaksByDate(any()) } just Runs
        coEvery { planActivityRepository.deletePlanActivitiesByDate(any()) } just Runs

        viewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)

        var callbackCalled = false

        viewModel.resetPlan {
            callbackCalled = true
        }

        advanceUntilIdle()

        coVerify { planRepository.deletePlan(any()) }
        coVerify { breakRepository.deleteBreaksByDate(any()) }
        coVerify { planActivityRepository.deletePlanActivitiesByDate(any()) }

        assertEquals(PlanState.NOT_STARTED, viewModel.planState)
        assertFalse(viewModel.isConfirmed)
        assertTrue(callbackCalled)
    }

    @Test
    fun `setEndTime should update repository and state`() = runTest {
        coEvery { planRepository.updateEndTime(any(), any()) } just Runs

        viewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)

        viewModel.setEndTime("22:00")

        advanceUntilIdle()

        coVerify { planRepository.updateEndTime(any(), "22:00") }
        assertEquals("22:00", viewModel.planEndTime)
    }

    @Test
    fun `loadPlan should set NOT_STARTED when no plan`() = runTest {
        coEvery { planRepository.getPlan(any()) } returns null

        viewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)

        advanceUntilIdle()

        assertEquals(PlanState.NOT_STARTED, viewModel.planState)
    }

    @Test
    fun `loadPlan should set CONFIRMED state`() = runTest {
        val fakePlan = PlanEntity(
            date = "2025-01-01",
            energyLevel = 10,
            isConfirmed = true,
            endTime = "23:00"
        )

        coEvery { planRepository.getPlan(any()) } returns fakePlan
        coEvery { planActivityRepository.getPlanActivitiesWithBreaks(any()) } returns emptyList()

        viewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)

        advanceUntilIdle()

        assertEquals(PlanState.CONFIRMED, viewModel.planState)
        assertTrue(viewModel.isConfirmed)
    }
}