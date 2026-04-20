package com.example.energymanagementapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.ui.screens.EnergyScreen
import com.example.energymanagementapp.ui.screens.PlanCreationHomeScreen
import com.example.energymanagementapp.ui.theme.EnergyManagementAppTheme
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
        var isEnergySet by mutableStateOf(false)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "plan_creation_home"
            ) {

                composable("plan_creation_home") {
                    PlanCreationHomeScreen(
                        energy = energyViewModel.energy,
                        isEnergySet = isEnergySet,
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
                            isEnergySet = true
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}