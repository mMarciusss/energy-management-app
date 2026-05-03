package com.example.energymanagementapp

import com.example.energymanagementapp.data.repository.WeatherRepository
import com.example.energymanagementapp.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
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
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel

    private val repository: WeatherRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWeather should set weather data`() = runTest {
        val fakeResponse = mockk<com.example.energymanagementapp.data.remote.weather.WeatherResponse>()

        val hourly = mockk<com.example.energymanagementapp.data.remote.weather.HourlyWeather>()

        coEvery { repository.getCurrentWeather(any(), any()) } returns fakeResponse

        coEvery { fakeResponse.hourly } returns hourly

        coEvery { hourly.time } returns listOf("2025-01-01T10:00", "2025-01-01T19:00")
        coEvery { hourly.temperature } returns listOf(10.0, 5.0)
        coEvery { hourly.weatherCode } returns listOf(1, 2)

        viewModel = WeatherViewModel(repository)

        viewModel.loadWeather()

        advanceUntilIdle()

        assertFalse(viewModel.isLoading)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `loadWeather should handle error`() = runTest {
        coEvery { repository.getCurrentWeather(any(), any()) } throws Exception()

        viewModel = WeatherViewModel(repository)

        viewModel.loadWeather()

        advanceUntilIdle()

        assertEquals("Failed to load weather", viewModel.errorMessage)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `loadWeather should set loading state`() = runTest {
        val fakeResponse = mockk<com.example.energymanagementapp.data.remote.weather.WeatherResponse>()
        val hourly = mockk<com.example.energymanagementapp.data.remote.weather.HourlyWeather>()

        coEvery { repository.getCurrentWeather(any(), any()) } returns fakeResponse
        coEvery { fakeResponse.hourly } returns hourly

        coEvery { hourly.time } returns emptyList()
        coEvery { hourly.temperature } returns emptyList()
        coEvery { hourly.weatherCode } returns emptyList()

        viewModel = WeatherViewModel(repository)

        viewModel.loadWeather()

        advanceUntilIdle()

        assertFalse(viewModel.isLoading)
    }
}