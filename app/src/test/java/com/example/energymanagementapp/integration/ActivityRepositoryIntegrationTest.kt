package com.example.energymanagementapp.integration

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.ActivityRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActivityRepositoryIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: ActivityRepository

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = ActivityRepository(db.activityDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `saveActivity and getActivityList should work correctly`() = runBlocking {
        repository.saveActivity("Test Activity", 3)

        val list = repository.getActivityList()

        assertEquals(1, list.size)
        assertEquals("Test Activity", list.first().name)
        assertEquals(3, list.first().energyCost)
    }

    @Test
    fun `deleteActivity should remove activity`() = runBlocking {
        repository.saveActivity("Test", 2)
        val activity = repository.getActivityList().first()

        repository.deleteActivity(activity)

        val list = repository.getActivityList()

        assertTrue(list.isEmpty())
    }

    @Test
    fun `seedActivitiesIfEmpty should insert default activities`() = runBlocking {
        var callbackCalled = false

        repository.seedActivitiesIfEmpty {
            callbackCalled = true
        }

        val list = repository.getActivityList()

        assertTrue(callbackCalled)
        assertEquals(5, list.size)
    }

    @Test
    fun `seedActivitiesIfEmpty should not override existing data`() = runBlocking {
        repository.saveActivity("Custom", 4)

        repository.seedActivitiesIfEmpty {}

        val list = repository.getActivityList()

        assertEquals(1, list.size)
        assertEquals("Custom", list.first().name)
    }
}