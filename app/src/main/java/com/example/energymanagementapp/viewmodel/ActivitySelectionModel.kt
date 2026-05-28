package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energymanagementapp.data.local.entities.ActivityEntity
import com.example.energymanagementapp.data.repository.ActivityRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import com.example.energymanagementapp.ui.localization.AppLanguage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivitySelectionModel (
    private val activityRepository: ActivityRepository,
    private val planActivityRepository: PlanActivityRepository,
) : ViewModel() {

    // visų galimų veiklų sarašas
    var activities by mutableStateOf<List<ActivityEntity>>(emptyList())
        private set

    // pasirinktų veiklų ID sąrašas
    var selectedActivities = mutableStateListOf<Int>()

    // pradinis vartotojo energijos kiekis
    var initialEnergy by mutableIntStateOf(0)
        private set

    // likęs energijos kiekis po veiklų pasirinkimo
    var remainingEnergy by mutableIntStateOf(0)
        private set

    // ar energijos reikšmė inicijuota
    var isIntialized by mutableStateOf(false)
        private set

    // dabartinė plano diena
    var currentDay by mutableStateOf("")

    // dabartinė kalba
    private var currentLanguage: AppLanguage = AppLanguage.EN

    init {
        // veiklų užkrovimas paleidimo metu
        loadActivities()
    }

    // visų veiklų užkrovimas
    private fun loadActivities() {
        viewModelScope.launch {
            val dbActivities = activityRepository.getActivityList()

            activities = if (dbActivities.isEmpty()) {
                activityRepository.getPresetActivities(currentLanguage)
            } else {
                dbActivities
            }
        }
    }

    // veiklų perkrovimas
    fun relaodActivities(language: AppLanguage = currentLanguage) {
        currentLanguage = language
        loadActivities()
    }

    // energijos reikšmės inicijavimas
    fun initEnergy(energy: Int){
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if(initialEnergy != energy) {
            isIntialized = false
        }

        if(currentDay != today){
            currentDay = today
            isIntialized = false
        }

        if(isIntialized) return

        isIntialized = true

        initialEnergy = energy
        remainingEnergy = energy

        loadSelectedActivitiesForToday()
    }

    // bendra pasirinktų veiklų energijos kaina
    fun getTotalSelectedEnergy(): Int{
        return activities
            .filter { selectedActivities.contains(it.id) }
            .sumOf { it.energyCost }
    }

    // veiklos pažymėjimas arba atžymėjimas
    fun toggleActivity(activity: ActivityEntity){
        if(selectedActivities.contains(activity.id)){
            selectedActivities.remove(activity.id)
            remainingEnergy += activity.energyCost
        } else {
            if(remainingEnergy >= activity.energyCost) {
                selectedActivities.add(activity.id)
                remainingEnergy -= activity.energyCost
            }
        }
    }

    // išsaugomos pasirinktos plano veiklos DB
    fun savePlanActivities(
        language: AppLanguage,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // pirmiausia į DB įrašomos visos preset veiklos
            activityRepository.persistPresetActivities(language)

            // po seed paimamos realios DB veiklos su tikrais ID
            val dbActivities = activityRepository.getActivityList()

            val selectedRealActivities = selectedActivities.mapNotNull { selectedId ->
                val uiActivity = activities.find { it.id == selectedId }

                if (uiActivity == null) {
                    null
                } else {
                    dbActivities.find {
                        it.name == uiActivity.name && it.energyCost == uiActivity.energyCost
                    }
                }
            }

            val existing = planActivityRepository
                .getPlanActivities(today)
                .map { it.activityId }

            val selectedRealIds = selectedRealActivities.map { it.id }

            val toRemove = existing - selectedRealIds
            val toAdd = selectedRealActivities.filter { it.id !in existing }

            toRemove.forEach { id ->
                planActivityRepository.deletePlanActivityByDateAndActivityId(today, id)
            }

            toAdd.forEach { activity ->
                planActivityRepository.savePlanActivity(
                    planDate = today,
                    activityId = activity.id,
                    activityName = activity.name,
                    energyCost = activity.energyCost,
                    isCompleted = false,
                    completionTime = null
                )
            }

            activities = dbActivities

            selectedActivities.clear()
            selectedActivities.addAll(selectedRealIds)

            onDone()
        }
    }

    // užkraunamos šiandien jau pasirinktos veiklos
    fun loadSelectedActivitiesForToday(){
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val existing = planActivityRepository.getPlanActivities(today)

            selectedActivities.clear()
            selectedActivities.addAll(existing.map {it.activityId})

            if(activities.isEmpty()) {
                activities = activityRepository.getActivityList()
            }

            val selected = activities.filter {selectedActivities.contains(it.id)}
            val usedEnergy = selected.sumOf {it.energyCost}

            remainingEnergy = initialEnergy - usedEnergy
        }
    }
}