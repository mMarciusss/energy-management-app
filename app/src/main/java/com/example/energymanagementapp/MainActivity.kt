package com.example.energymanagementapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.remote.weather.WeatherRetrofitInstance
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.data.repository.WeatherRepository
import com.example.energymanagementapp.ui.accessibility.AccessibilitySettings
import com.example.energymanagementapp.ui.accessibility.LocalAccessibilitySettings
import com.example.energymanagementapp.ui.screens.ActivityBreakListScreen
import com.example.energymanagementapp.ui.screens.ActivitySelectionScreen
import com.example.energymanagementapp.ui.screens.BreakSetupScreen
import com.example.energymanagementapp.ui.screens.BreakTimerScreen
import com.example.energymanagementapp.ui.screens.DaySummaryScreen
import com.example.energymanagementapp.ui.screens.EnergyScreen
import com.example.energymanagementapp.ui.screens.HomeScreen
import com.example.energymanagementapp.ui.screens.PlanCreationHomeScreen
import com.example.energymanagementapp.ui.screens.PlanExecutionScreen
import com.example.energymanagementapp.ui.screens.ManageActivitiesScreen
import com.example.energymanagementapp.ui.screens.PastDaysScreen
import com.example.energymanagementapp.viewmodel.ActivityManagementViewModel
import com.example.energymanagementapp.viewmodel.ActivitySelectionModel
import com.example.energymanagementapp.viewmodel.BreakViewModel
import com.example.energymanagementapp.viewmodel.DaySummaryViewModel
import com.example.energymanagementapp.viewmodel.EnergyViewModel
import com.example.energymanagementapp.viewmodel.PastDaysViewModel
import com.example.energymanagementapp.viewmodel.PlanViewModel
import com.example.energymanagementapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // sukuriama lokali Room duomenų bazė
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "energyManagement.db"
        )
        .fallbackToDestructiveMigration(false)
        .build()

        // sukuriami repository objektai darbui su DB
        val activityRepository = ActivityRepository(db.activityDao())
        val planActivityRepository = PlanActivityRepository(db.planActivityDao())
        val breakRepository = BreakRepository(db.breakDao())
        val planRepository = PlanRepository(db.planDao())

        // sukuriami viewmodel objektai pogramėlės būsenai valdyti
        val activitySelectionModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        val daySummaryViewModel = DaySummaryViewModel(planActivityRepository)
        val pastDaysViewModel = PastDaysViewModel(planActivityRepository)
        val activityManagementViewModel = ActivityManagementViewModel(activityRepository)
        val breakViewModel = BreakViewModel(planActivityRepository, breakRepository)
        val planViewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)
        val energyViewModel = EnergyViewModel(planRepository)

        // sukuriamas orų API repository ir viewmodel
        val weatherRepository = WeatherRepository(WeatherRetrofitInstance.api)
        val weatherViewModel = WeatherViewModel(weatherRepository)

        // pradinių veiklų įrašymas į DB jeigu jų nėra
        lifecycleScope.launch{
            activityRepository.seedActivitiesIfEmpty(){
                activitySelectionModel.relaodActivities()
                activityManagementViewModel.refreshActivities()
            }
        }

        setContent {
            val navController = rememberNavController()
            var accessibilityMode by remember { mutableStateOf(false) }

            CompositionLocalProvider(
                LocalAccessibilitySettings provides AccessibilitySettings(accessibilityMode)
            ) {

                // pagrindinė navigacijos struktūra
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {

                        // užkraunami visi reikalingi duomenys prieš atidarant pagrindinį ekraną
                        LaunchedEffect(Unit) {
                            planViewModel.reloadPlan()
                            activitySelectionModel.initEnergy(energyViewModel.energy)
                            breakViewModel.reloadPlanActivities()
                            weatherViewModel.loadWeather()
                        }

                        // pagrindinio ekrano sukūrimas
                        HomeScreen(
                            planState = planViewModel.planState,
                            isTooLateToStart = planViewModel.isTooLateToStart,
                            accessibilityMode = accessibilityMode,
                            onStartPlan = {
                                navController.navigate("plan_creation_home")
                            },
                            onContinuePlan = {
                                navController.navigate("plan_creation_home")
                            },
                            onViewPlan = {
                                navController.navigate("plan_execution")
                            },
                            onViewSummary = {
                                navController.navigate("day_summary")
                            },
                            onViewPastDays = {
                                navController.navigate("past_days")
                            },
                            onManageActivities = {
                                navController.navigate("manage_activities")
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("plan_creation_home") {

                        // plano kūrimo ekrano sukūrimas
                        PlanCreationHomeScreen(
                            energy = energyViewModel.energy,
                            isEnergySet = energyViewModel.isEnergySet,
                            endTime = planViewModel.planEndTime,
                            planState = planViewModel.planState,
                            weatherTemperature = weatherViewModel.weatherNow?.first,
                            weatherCode = weatherViewModel.weatherNow?.second,
                            accessibilityMode = accessibilityMode,
                            onGoHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onCancelPlan = {
                                planViewModel.resetPlan() {
                                    energyViewModel.reloadEnergy()
                                    breakViewModel.reloadPlanActivities()
                                    activitySelectionModel.loadSelectedActivitiesForToday()
                                }

                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onGoToEnergyScreen = {
                                navController.navigate("energy")
                            },
                            onGoToActivitySelection = {
                                navController.navigate("activity_selection")
                            },
                            onGoToBreakScreen = {
                                navController.navigate("assign_break")
                            },
                            onConfirmPlan = {
                                val start = System.currentTimeMillis()
                                planViewModel.confirmPlan {
                                    val end = System.currentTimeMillis()
                                    Log.d("PERF", "Plan creation time: ${end - start} ms")
                                    navController.navigate("plan_execution") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            },
                            selectedActivities = breakViewModel.planActivities
                        )
                    }

                    composable("energy") {

                        val minRequiredEnergy = activitySelectionModel.getTotalSelectedEnergy()

                        // energijos nustatymo ekrano sukūrimas
                        EnergyScreen(
                            energy = energyViewModel.energy,
                            minEnergy = maxOf(3, minRequiredEnergy),
                            endTime = planViewModel.planEndTime,
                            accessibilityMode = accessibilityMode,
                            onIncrease = { energyViewModel.increaseEnergy() },
                            onDecrease = { energyViewModel.decreaseEnergy() },
                            onConfirm = { endTime ->
                                energyViewModel.saveEnergy() {
                                    planViewModel.setEndTime(endTime)
                                    planViewModel.startCreatingPlan()
                                }

                                navController.popBackStack()
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("activity_selection") {

                        activitySelectionModel.initEnergy(energyViewModel.energy)

                        LaunchedEffect(Unit) {
                            activitySelectionModel.relaodActivities()
                        }

                        // veiklų pasirinkimo ekrano sukūrimas
                        ActivitySelectionScreen(
                            activities = activitySelectionModel.activities,
                            selectedActivities = activitySelectionModel.selectedActivities,
                            remainingEnergy = activitySelectionModel.remainingEnergy,
                            totalEnergy = energyViewModel.energy,
                            weatherNow = weatherViewModel.weatherNow,
                            weatherIn3Hours = weatherViewModel.weatherIn3Hours,
                            weatherEvening = weatherViewModel.weatherEvening,
                            accessibilityMode = accessibilityMode,
                            onToggle = { activitySelectionModel.toggleActivity(it) },
                            onConfirm = {
                                activitySelectionModel.savePlanActivities {
                                    breakViewModel.reloadPlanActivities()
                                    navController.popBackStack()
                                }
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("assign_break") {
                        breakViewModel.reloadPlanActivities()

                        // pertaukų priskyrimo veikloms ekrano sukūrimas
                        ActivityBreakListScreen(
                            planActivities = breakViewModel.planActivities,
                            accessibilityMode = accessibilityMode,
                            onActivityClick = { planActivityId, planActivityName ->
                                breakViewModel.loadBreak(planActivityId)
                                navController.navigate("break_setup/$planActivityId/$planActivityName")
                            },
                            onBackToPlanCreation = {
                                navController.popBackStack("plan_creation_home", false)
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("break_setup/{planActivityId}/{planActivityName}") { backStackEntry ->

                        val planActivityId =
                            backStackEntry.arguments?.getString("planActivityId")?.toInt() ?: 0
                        val planActivityName =
                            backStackEntry.arguments?.getString("planActivityName") ?: ""

                        // pertraukų nustatymo ekrano sukūrimas
                        BreakSetupScreen(
                            activityName = planActivityName,
                            breakDuration = breakViewModel.breakDuration,
                            hasBreak = breakViewModel.hasBreak,
                            accessibilityMode = accessibilityMode,

                            onIncrease = { breakViewModel.increaseBreakDuration() },
                            onDecrease = { breakViewModel.decreaseBreakDuration() },
                            onConfirm = {
                                breakViewModel.createBreak(planActivityId)
                                breakViewModel.reloadPlanActivities()
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            },
                            onRemove = {
                                breakViewModel.removeBreak(planActivityId)
                                navController.popBackStack()
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("plan_execution") {

                        // užkraunami plano vykdymo duomenys
                        breakViewModel.setEnergy(energyViewModel.energy)
                        breakViewModel.reloadPlanActivities()

                        val runningBreakId = breakViewModel.getRunningBreakActivityId()
                        val allCompleted = planViewModel.isAllCompleted
                        val isExpired = planViewModel.isExpired

                        // jei plano laikas pasibaigė, rodoma dienos apžvalga
                        LaunchedEffect(isExpired) {
                            if (isExpired) {
                                navController.navigate("day_summary") {
                                    popUpTo("plan_execution") { inclusive = true }
                                }
                            }
                        }

                        // jei visos veiklos atliktos, rodoma dienos apžvalga
                        LaunchedEffect(allCompleted) {
                            if (allCompleted) {
                                navController.navigate("day_summary") {
                                    popUpTo("plan_execution") { inclusive = true }
                                }
                            }
                        }

                        // jei yra pradėta pertrauka, atidaromas laikmatis
                        LaunchedEffect(runningBreakId) {
                            if (runningBreakId != null) {
                                navController.navigate("timer/$runningBreakId")
                            }
                        }
                        if (runningBreakId == null && !allCompleted) {

                            // plano vykdymo ekrano sukūrimas
                            PlanExecutionScreen(
                                energy = breakViewModel.remainingEnergy,
                                totalEnergy = energyViewModel.energy,
                                activities = breakViewModel.planActivities,
                                weatherNow = weatherViewModel.weatherNow,
                                weatherIn3Hours = weatherViewModel.weatherIn3Hours,
                                weatherEvening = weatherViewModel.weatherEvening,
                                accessibilityMode = accessibilityMode,
                                onConfirmComplete = { ids ->
                                    breakViewModel.completeActivities(ids) { breakActivityId ->

                                        if (breakActivityId != null) {
                                            breakViewModel.startBreakTimer(breakActivityId) {
                                                navController.navigate("timer/$breakActivityId")
                                            }
                                        } else {
                                            if (breakViewModel.areAllActivitiesCompleted()) {
                                                navController.navigate("day_summary")
                                            }
                                        }
                                    }
                                },
                                onGoHome = {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onCancelPlan = {
                                    planViewModel.resetPlan {
                                        energyViewModel.reloadEnergy()
                                        breakViewModel.reloadPlanActivities()
                                        activitySelectionModel.loadSelectedActivitiesForToday()
                                    }

                                    navController.navigate("plan_creation_home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onToggleAccessibility = {
                                    accessibilityMode = !accessibilityMode
                                }
                            )
                        }
                    }

                    composable("timer/{planActivityId}") { backStackEntry ->
                        val id =
                            backStackEntry.arguments?.getString("planActivityId")?.toIntOrNull()
                                ?: 0

                        val activity = breakViewModel.planActivities.find { it.id == id }

                        // pertraukos laikmačio ekrano sukūrimas
                        BreakTimerScreen(
                            endTime = activity?.endTime ?: 0L,
                            accessibilityMode = accessibilityMode,
                            onFinish = {
                                breakViewModel.completeAfterBreak(id) {
                                    if (breakViewModel.areAllActivitiesCompleted()) {
                                        navController.navigate("day_summary")
                                    } else {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("day_summary") {

                        // užkraunami dienos ataskaitos duomenys
                        LaunchedEffect(Unit) {
                            daySummaryViewModel.loadSummary(planViewModel.getToday())
                        }

                        // dienos ataskaitos ekrano sukūrimas
                        DaySummaryScreen(
                            activities = daySummaryViewModel.activities,
                            totalEnergy = energyViewModel.energy,
                            totalEnergyUsed = daySummaryViewModel.totalEnergyUsed,
                            totalRestTimeMinutes = daySummaryViewModel.totalRestTimeMinutes,
                            isFromCalendar = false,
                            accessibilityMode = accessibilityMode,
                            onGoHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("past_days") {

                        // užkraunami ankstesnių dienų ataskaitų duomenys
                        LaunchedEffect(Unit) {
                            pastDaysViewModel.loadDayStatuses()
                        }

                        // ankstesnių dienos ataskaitų ekrano sukūrimas
                        PastDaysScreen(
                            dayStatuses = pastDaysViewModel.dayStatuses,
                            accessibilityMode = accessibilityMode,
                            onDateClick = { date ->
                                navController.navigate("day_summary/$date?fromCalendar=true")
                            },
                            onGoHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }

                    composable("day_summary/{date}?fromCalendar={fromCalendar}") { backStackEntry ->
                        val date = backStackEntry.arguments?.getString("date") ?: ""
                        val fromCalendar =
                            backStackEntry.arguments?.getString("fromCalendar") == "true"

                        // užkraunami dienos ataskaitos duomenys
                        LaunchedEffect(date) {
                            daySummaryViewModel.loadSummary(date)
                        }

                        // dienos ataskaitos ekrano sukūrimas
                        DaySummaryScreen(
                            activities = daySummaryViewModel.activities,
                            totalEnergy = energyViewModel.energy,
                            totalEnergyUsed = daySummaryViewModel.totalEnergyUsed,
                            totalRestTimeMinutes = daySummaryViewModel.totalRestTimeMinutes,
                            isFromCalendar = fromCalendar,
                            accessibilityMode = accessibilityMode,
                            onGoHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onGoBack = {
                                navController.popBackStack()
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )

                    }

                    composable("manage_activities") {

                        // veiklų redagavimo ekrano sukūrimas
                        ManageActivitiesScreen(
                            activities = activityManagementViewModel.activities,
                            accessibilityMode = accessibilityMode,
                            onBackToHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onAdd = { name, energyCost ->
                                activityManagementViewModel.addActivity(name, energyCost)
                            },
                            onDelete = { activity ->
                                activityManagementViewModel.deleteActivity(activity)
                            },
                            onToggleAccessibility = {
                                accessibilityMode = !accessibilityMode
                            }
                        )
                    }
                }
            }
        }
    }
}