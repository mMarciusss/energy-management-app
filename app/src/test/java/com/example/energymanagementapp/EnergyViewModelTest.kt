package com.example.energymanagementapp

import androidx.compose.ui.Modifier.Companion.any
import com.example.energymanagementapp.data.local.entities.PlanEntity
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.viewmodel.EnergyViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class EnergyViewModelTest {

    private lateinit var viewModel: EnergyViewModel
    private val repository: PlanRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `increaseEnergy should increase energy by 1`() {
        viewModel = EnergyViewModel(repository)

        val initial = viewModel.energy

        viewModel.increaseEnergy()

        assertEquals(initial + 1, viewModel.energy)
    }

    @Test
    fun `increaseEnergy should not exceed 20`() {
        viewModel = EnergyViewModel(repository)

        repeat(20) { viewModel.increaseEnergy() }

        assertEquals(20, viewModel.energy)
    }

    @Test
    fun `decreaseEnergy should decrease energy by 1`() {
        viewModel = EnergyViewModel(repository)

        viewModel.increaseEnergy()
        val initial = viewModel.energy

        viewModel.decreaseEnergy()

        assertEquals(initial - 1, viewModel.energy)
    }

    @Test
    fun `decreaseEnergy should not go below 3`() {
        viewModel = EnergyViewModel(repository)

        repeat(10) { viewModel.decreaseEnergy() }

        assertEquals(3, viewModel.energy)
    }

    @Test
    fun `saveEnergy should call repository and set isEnergySet`() = runTest {
        coEvery { repository.saveEnergy(any(), any()) } just Runs

        viewModel = EnergyViewModel(repository)

        var callbackCalled = false

        viewModel.saveEnergy {
            callbackCalled = true
        }

        advanceUntilIdle()

        coVerify { repository.saveEnergy(any(), viewModel.energy) }
        assertTrue(viewModel.isEnergySet)
        assertTrue(callbackCalled)
    }

    @Test
    fun `loadTodayEnergy should set energy from plan`() = runTest {
        val fakePlan = PlanEntity(
            date = "2025-01-01",
            energyLevel = 10
        )

        coEvery { repository.getPlan(any()) } returns fakePlan

        viewModel = EnergyViewModel(repository)

        advanceUntilIdle()

        assertEquals(10, viewModel.energy)
        assertTrue(viewModel.isEnergySet)
    }

    @Test
    fun `loadTodayEnergy should set default energy when no plan`() = runTest {
        coEvery { repository.getPlan(any()) } returns null

        viewModel = EnergyViewModel(repository)

        advanceUntilIdle()

        assertEquals(5, viewModel.energy)
        assertFalse(viewModel.isEnergySet)
    }
}