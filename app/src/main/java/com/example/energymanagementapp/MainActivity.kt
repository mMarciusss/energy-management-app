package com.example.energymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.model.PlanActivityWithDetails
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.ui.screens.ActivityBreakListScreen
import com.example.energymanagementapp.ui.screens.ActivitySelectionScreen
import com.example.energymanagementapp.ui.screens.BreakSetupScreen
import com.example.energymanagementapp.ui.screens.BreakTimerScreen
import com.example.energymanagementapp.ui.screens.DaySummaryScreen
import com.example.energymanagementapp.ui.screens.EnergyScreen
import com.example.energymanagementapp.ui.screens.PlanCreationHomeScreen
import com.example.energymanagementapp.ui.screens.PlanExecutionScreen
import com.example.energymanagementapp.viewmodel.ActivitySelectionModel
import com.example.energymanagementapp.viewmodel.BreakViewModel
import com.example.energymanagementapp.viewmodel.DaySummaryViewModel
import com.example.energymanagementapp.viewmodel.EnergyViewModel
import com.example.energymanagementapp.viewmodel.PlanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        val planRepository = PlanRepository(db.planDao())
        val planViewModel = PlanViewModel(planRepository)
        val energyViewModel = EnergyViewModel(planRepository)

        val activityRepository = ActivityRepository(db.activityDao())
        val planActivityRepository = PlanActivityRepository(db.planActivityDao())
        val activitySelectionModel = ActivitySelectionModel(activityRepository, planActivityRepository)
        val daySummaryViewModel = DaySummaryViewModel(planActivityRepository)

        val breakRepository = BreakRepository(db.breakDao())
        val breakViewModel = BreakViewModel(planActivityRepository, breakRepository)


        setContent {
            val navController = rememberNavController()

            var startDestination by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(planViewModel.isConfirmed, planViewModel.isExpired) {
                startDestination =
                    if(planViewModel.isConfirmed) "plan_execution"
                    else if(planViewModel.isExpired) "day_summary"
                    else "plan_creation_home"
            }

            if(startDestination != null){
                NavHost(
                    navController = navController,
                    startDestination = startDestination!!
                ) {

                    composable("plan_creation_home") {
                        PlanCreationHomeScreen(
                            energy = energyViewModel.energy,
                            isEnergySet = energyViewModel.isEnergySet,
                            endTime = planViewModel.planEndTime,
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
//                                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//
//                                CoroutineScope(Dispatchers.IO).launch {
//                                    planRepository.confirmPlan(today)
//                                }
//
//                                navController.navigate("plan_execution")
                                planViewModel.confirmPlan {
                                    navController.navigate("plan_execution")
                                }
                            },
                            selectedActivities = breakViewModel.planActivities
                        )
                    }

                    composable("energy") {
                        EnergyScreen(
                            energy = energyViewModel.energy,
                            endTime = planViewModel.planEndTime,
                            onIncrease = {energyViewModel.increaseEnergy()},
                            onDecrease = {energyViewModel.decreaseEnergy()},
                            onConfirm = { endTime ->
                                energyViewModel.saveEnergy()
                                planViewModel.setEndTime(endTime)

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
                            onBackToHome = {
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
                            onIncrease = {breakViewModel.increaseBreakDuration()},
                            onDecrease = {breakViewModel.decreaseBreakDuration()},
                            onConfirm = {
                                breakViewModel.createBreak(planActivityId)
                                breakViewModel.reloadPlanActivities()
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("plan_execution") {
                        breakViewModel.setEnergy(energyViewModel.energy)
                        breakViewModel.reloadPlanActivities()

                        val runningBreakId = breakViewModel.getRunningBreakActivityId()
                        val allCompleted = breakViewModel.areAllActivitiesCompleted()
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
                            daySummaryViewModel.loadSummary()
                        }

                        DaySummaryScreen(
                            activities = daySummaryViewModel.activities,
                            totalEnergyUsed = daySummaryViewModel.totalEnergyUsed,
                            totalRestTimeMinutes = daySummaryViewModel.totalRestTimeMinutes
                        )
                    }
                }
            }
        }
    }
}