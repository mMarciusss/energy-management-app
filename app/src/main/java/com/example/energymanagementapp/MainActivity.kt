package com.example.energymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
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
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "energyManagement.db"
        )
        .fallbackToDestructiveMigration(false)
        .build()

        val activityRepository = ActivityRepository(db.activityDao())
        val planActivityRepository = PlanActivityRepository(db.planActivityDao())
        val activitySelectionModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        val daySummaryViewModel = DaySummaryViewModel(planActivityRepository)
        val pastDaysViewModel = PastDaysViewModel(planActivityRepository)
        val activityManagementViewModel = ActivityManagementViewModel(activityRepository)

        val breakRepository = BreakRepository(db.breakDao())
        val breakViewModel = BreakViewModel(planActivityRepository, breakRepository)

        val planRepository = PlanRepository(db.planDao())
        val planViewModel = PlanViewModel(planRepository, breakRepository, planActivityRepository)
        val energyViewModel = EnergyViewModel(planRepository)


        val weatherRepository = WeatherRepository(WeatherRetrofitInstance.api)
        val weatherViewModel = WeatherViewModel(weatherRepository)

        lifecycleScope.launch{
            activityRepository.seedActivitiesIfEmpty()
            activityManagementViewModel.refreshActivities()
        }

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {

                    LaunchedEffect(Unit) {
                        planViewModel.reloadPlan()
                        activitySelectionModel.initEnergy(energyViewModel.energy)
                        breakViewModel.reloadPlanActivities()
                        weatherViewModel.loadWeather()
                    }

                    HomeScreen(
                        planState = planViewModel.planState,
                        isTooLateToStart = planViewModel.isTooLateToStart,
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
                        }
                    )
                }

                composable("plan_creation_home") {
                    PlanCreationHomeScreen(
                        energy = energyViewModel.energy,
                        isEnergySet = energyViewModel.isEnergySet,
                        endTime = planViewModel.planEndTime,
                        planState = planViewModel.planState,
                        weatherTemperature = weatherViewModel.weatherNow?.first,
                        weatherCode = weatherViewModel.weatherNow?.second,
                        onGoHome = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        onCancelPlan = {
                            planViewModel.resetPlan(){
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
                            planViewModel.confirmPlan {
                                navController.navigate("plan_execution") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        },
                        selectedActivities = breakViewModel.planActivities
                    )
                }

                composable("energy") {

                    val minRequiredEnergy = activitySelectionModel.getTotalSelectedEnergy()

                    EnergyScreen(
                        energy = energyViewModel.energy,
                        minEnergy = maxOf(3, minRequiredEnergy),
                        endTime = planViewModel.planEndTime,
                        onIncrease = {energyViewModel.increaseEnergy()},
                        onDecrease = {energyViewModel.decreaseEnergy()},
                        onConfirm = { endTime ->
                            energyViewModel.saveEnergy(){
                                planViewModel.setEndTime(endTime)
                                planViewModel.startCreatingPlan()
                            }

                            navController.popBackStack()
                        }
                    )
                }

                composable("activity_selection") {

                    activitySelectionModel.initEnergy(energyViewModel.energy)

                    ActivitySelectionScreen(
                        activities = activitySelectionModel.activities,
                        selectedActivities = activitySelectionModel.selectedActivities,
                        remainingEnergy = activitySelectionModel.remainingEnergy,
                        weatherNow = weatherViewModel.weatherNow,
                        weatherIn3Hours = weatherViewModel.weatherIn3Hours,
                        weatherEvening = weatherViewModel.weatherEvening,
                        onToggle = {activitySelectionModel.toggleActivity(it)},
                        onConfirm = {
                            activitySelectionModel.savePlanActivities {
                                breakViewModel.reloadPlanActivities()
                                navController.popBackStack()
                            }
                        }
                    )
                }

                composable("assign_break"){
                    breakViewModel.reloadPlanActivities()

                    ActivityBreakListScreen(
                        planActivities = breakViewModel.planActivities,
                        onActivityClick = { planActivityId, planActivityName ->
                            breakViewModel.loadBreak(planActivityId)
                            navController.navigate("break_setup/$planActivityId/$planActivityName")
                        },
                        onBackToPlanCreation = {
                            navController.popBackStack("plan_creation_home", false)
                        }
                    )
                }

                composable("break_setup/{planActivityId}/{planActivityName}") { backStackEntry ->

                    val planActivityId = backStackEntry.arguments?.getString("planActivityId")?.toInt() ?: 0
                    val planActivityName = backStackEntry.arguments?.getString("planActivityName") ?: ""

                    BreakSetupScreen(
                        activityName = planActivityName,
                        breakDuration = breakViewModel.breakDuration,
                        hasBreak = breakViewModel.hasBreak,

                        onIncrease = {breakViewModel.increaseBreakDuration()},
                        onDecrease = {breakViewModel.decreaseBreakDuration()},
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
                        }
                    )
                }

                composable("plan_execution") {
                    breakViewModel.setEnergy(energyViewModel.energy)
                    breakViewModel.reloadPlanActivities()

                    val runningBreakId = breakViewModel.getRunningBreakActivityId()
                    val allCompleted = planViewModel.isAllCompleted
                    val isExpired = planViewModel.isExpired

                    LaunchedEffect(isExpired) {
                        if(isExpired) {
                            navController.navigate("day_summary") {
                                popUpTo("plan_execution") {inclusive = true}
                            }
                        }
                    }

                    LaunchedEffect(allCompleted) {
                        if(allCompleted) {
                            navController.navigate("day_summary") {
                                popUpTo("plan_execution") {inclusive = true}
                            }
                        }
                    }

                    LaunchedEffect(runningBreakId) {
                        if (runningBreakId != null) {
                            navController.navigate("timer/$runningBreakId")
                        }
                    }
                    if (runningBreakId == null && !allCompleted) {
                        PlanExecutionScreen(
                            energy = breakViewModel.remainingEnergy,
                            activities = breakViewModel.planActivities,
                            weatherNow = weatherViewModel.weatherNow,
                            weatherIn3Hours = weatherViewModel.weatherIn3Hours,
                            weatherEvening = weatherViewModel.weatherEvening,
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
                                    popUpTo("home") {inclusive = true}
                                }
                            }
                        )
                    }
                }

                composable("timer/{planActivityId}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("planActivityId")?.toIntOrNull() ?: 0

                    val activity = breakViewModel.planActivities.find {it.id == id}

                    BreakTimerScreen(
                        endTime = activity?.endTime ?: 0L,
                        onFinish = {
                            breakViewModel.completeAfterBreak(id) {
                                if(breakViewModel.areAllActivitiesCompleted()) {
                                    navController.navigate("day_summary")
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                }

                composable("day_summary") {
                    LaunchedEffect(Unit) {
                        daySummaryViewModel.loadSummary(planViewModel.getToday())
                    }

                    DaySummaryScreen(
                        activities = daySummaryViewModel.activities,
                        totalEnergy = energyViewModel.energy,
                        totalEnergyUsed = daySummaryViewModel.totalEnergyUsed,
                        totalRestTimeMinutes = daySummaryViewModel.totalRestTimeMinutes,
                        isFromCalendar = false,
                        onGoHome = {
                            navController.navigate("home") {
                                popUpTo("home") {inclusive = true}
                            }
                        }
                    )
                }

                composable("past_days") {
                    LaunchedEffect(Unit) {
                        pastDaysViewModel.loadDayStatuses()
                    }

                    PastDaysScreen(
                        dayStatuses = pastDaysViewModel.dayStatuses,
                        onDateClick = { date ->
                            navController.navigate("day_summary/$date?fromCalendar=true")
                        },
                        onGoHome = {
                            navController.navigate("home") {
                                popUpTo("home") {inclusive = true}
                            }
                        }
                    )
                }

                composable("day_summary/{date}?fromCalendar={fromCalendar}") { backStackEntry ->
                    val date = backStackEntry.arguments?.getString("date") ?: ""
                    val fromCalendar = backStackEntry.arguments?.getString("fromCalendar") == "true"


                    LaunchedEffect(date) {
                        daySummaryViewModel.loadSummary(date)
                    }

                    DaySummaryScreen(
                        activities = daySummaryViewModel.activities,
                        totalEnergy = energyViewModel.energy,
                        totalEnergyUsed = daySummaryViewModel.totalEnergyUsed,
                        totalRestTimeMinutes = daySummaryViewModel.totalRestTimeMinutes,
                        isFromCalendar = fromCalendar,
                        onGoHome = {
                            navController.navigate("home") {
                                popUpTo("home") {inclusive = true}
                            }
                        },
                        onGoBack = {
                            navController.popBackStack()
                        }
                    )

                }

                composable("manage_activities") {
                    ManageActivitiesScreen(
                        activities = activityManagementViewModel.activities,
                        onBackToHome = {
                            navController.navigate("home") {
                                popUpTo("home") {inclusive = true}
                            }
                        },
                        onAdd = { name, energyCost ->
                            activityManagementViewModel.addActivity(name, energyCost)
                        },
                        onDelete = { activity ->
                            activityManagementViewModel.deleteActivity(activity)
                        }
                    )
                }
            }
        }
    }
}