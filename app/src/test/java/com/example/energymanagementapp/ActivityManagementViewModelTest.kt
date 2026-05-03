package com.example.energymanagementapp

import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.viewmodel.ActivityManagementViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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
class ActivityManagementViewModelTest {

    private lateinit var viewModel: ActivityManagementViewModel
    private val repository: ActivityRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load activities on init`() = runTest {
        val list = listOf(ActivityEntity(1, "A", 3))

        coEvery { repository.getActivityList() } returns list

        viewModel = ActivityManagementViewModel(repository)

        advanceUntilIdle()

        assertEquals(1, viewModel.activities.size)
    }

    @Test
    fun `addActivity should call repository and refresh list`() = runTest {
        coEvery { repository.saveActivity(any(), any()) } just Runs
        coEvery { repository.getActivityList() } returns listOf(ActivityEntity(1, "A", 3))

        viewModel = ActivityManagementViewModel(repository)

        viewModel.addActivity("Test", 3)

        advanceUntilIdle()

        coVerify { repository.saveActivity("Test", 3) }
        assertEquals(1, viewModel.activities.size)
    }

    @Test
    fun `addActivity should NOT call repository if energy invalid`() = runTest {
        viewModel = ActivityManagementViewModel(repository)

        viewModel.addActivity("Bad", 10)

        advanceUntilIdle()

        coVerify(exactly = 0) { repository.saveActivity(any(), any()) }
    }

    @Test
    fun `deleteActivity should call repository and refresh`() = runTest {
        val activity = ActivityEntity(1, "A", 3)

        coEvery { repository.deleteActivity(any()) } just Runs
        coEvery { repository.getActivityList() } returns emptyList()

        viewModel = ActivityManagementViewModel(repository)

        viewModel.deleteActivity(activity)

        advanceUntilIdle()

        coVerify { repository.deleteActivity(activity) }
        assertEquals(0, viewModel.activities.size)
    }
}