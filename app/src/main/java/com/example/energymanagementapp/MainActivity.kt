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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.energymanagementapp.data.local.database.AppDatabase
import com.example.energymanagementapp.data.repository.PlanRepository
import com.example.energymanagementapp.ui.screens.EnergyScreen
import com.example.energymanagementapp.ui.theme.EnergyManagementAppTheme
import com.example.energymanagementapp.viewmodel.EnergyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "energyManagement.db"
        ).build()

        val repository = PlanRepository(db.planDao())
        val viewModel = EnergyViewModel(repository)

        setContent {
            EnergyScreen(viewModel)
        }
    }
}