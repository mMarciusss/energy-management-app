package com.example.energymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.ui.screens.EnergyScreen
import com.example.energymanagementapp.ui.screens.PlanCreationHomeScreen
import com.example.energymanagementapp.viewmodel.EnergyViewModel

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

        val repository = PlanRepository(db.planDao())
        val energyViewModel = EnergyViewModel(repository)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "plan_creation_home"
            ) {

                composable("plan_creation_home") {
                    PlanCreationHomeScreen(
                        energy = energyViewModel.energy,
                        isEnergySet = energyViewModel.isEnergySet,
                        onGoToEnergyScreen = {
                            navController.navigate("energy")
                        }
                    )
                }

                composable("energy") {
                    EnergyScreen(
                        energy = energyViewModel.energy,
                        onIncrease = {energyViewModel.increaseEnergy()},
                        onDecrease = {energyViewModel.decreaseEnergy()},
                        onConfirm = {
                            energyViewModel.saveEnergy()
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}