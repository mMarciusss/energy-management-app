package com.example.energymanagementapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.performInTransactionSuspending
import com.example.energymanagementapp.data.model.PlanActivityWithBreak
import com.example.energymanagementapp.data.model.PlanActivityWithDetails
import com.example.energymanagementapp.data.repository.BreakRepository
import com.example.energymanagementapp.data.repository.PlanActivityRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BreakViewModel (
    private val planActivityRepository: PlanActivityRepository,
    private val breakRepository: BreakRepository,
) : ViewModel() {

    // plano veiklos kartu su priskirtomis pertraukomis
    var planActivities by mutableStateOf<List<PlanActivityWithBreak>>(emptyList())

    // nustatyta pertraukos trukmė
    var breakDuration by mutableIntStateOf(30)
        private set

    // likęs energijos kiekis vykdant planą
    var remainingEnergy by mutableStateOf(0)
        private set

    // bendras vartotojo nustatytas energijos kiekis
    var totalEnergy by mutableStateOf(0)
        private set

    // ar pasirinkta veikla turi pertrauką
    var hasBreak by mutableStateOf(false)
        private set


    init {
        // užkraunamos plano veiklos paleidimo metu
        loadPlanActivities()
    }

    // visų plano veiklų užkrovimas
    private fun loadPlanActivities() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            planActivities = planActivityRepository.getPlanActivitiesWithBreaks(today)
        }
    }

    // plano veiklų perkrovimas
    fun reloadPlanActivities() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val list = planActivityRepository.getPlanActivitiesWithBreaks(today)

            val usedEnergy = list
                .filter { it.isCompleted}
                .sumOf { it.energyCost }

            remainingEnergy = totalEnergy - usedEnergy

            planActivities = list
        }
    }

    // nustatomas bendras energijos kiekis
    fun setEnergy(energy: Int){
        totalEnergy = energy
    }

    // pertraukos trukmės didinimas
    fun increaseBreakDuration(){
        if(breakDuration <=175)
            breakDuration += 5
    }

    // pertraukos trukmės mažinimas
    fun decreaseBreakDuration(){
        if(breakDuration > 5)
            breakDuration -= 5
    }

    // pertraukos pasirinktai veiklai išsaugojimas DB
    fun createBreak(planActivityId: Int){
        viewModelScope.launch {
            breakRepository.saveBreak(
                planActivityId = planActivityId,
                durationMinutes = breakDuration,
                startTime = 0L,
                endTime = 0L,
                isCompleted = false
            )
        }
    }

    // pertraukos pasirinktai veiklai ištrynimas DB
    fun removeBreak(planActivityId: Int) {
        viewModelScope.launch {
            breakRepository.deleteBreak(planActivityId)
            reloadPlanActivities()
        }
    }

    // veiklai priskirtos pertraukos užkrovimas
    fun loadBreak(planActivityId: Int){
        viewModelScope.launch {
            val existing = breakRepository.getBreak(planActivityId)

            if(existing != null) {
                breakDuration = existing.durationMinutes
                hasBreak = true
            } else {
                breakDuration = 30
                hasBreak = false
            }
        }
    }

    // veiklų žymėjimas kaip atliktos veiklos
    fun completeActivities(ids: List<Int>, onBreakNeeded: (Int?) -> Unit) {
        viewModelScope.launch {

            val selected = planActivities.filter { ids.contains(it.id) }
            val withBreak = selected.filter { it.breakDuration != null }
            val withoutBreak = selected.filter { it.breakDuration == null }
            val completionTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            withoutBreak.forEach {
                planActivityRepository.completeActivity(it.id, completionTime)
            }

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val updatedList = planActivityRepository.getPlanActivitiesWithBreaks(today)

            val usedEnergy = updatedList
                .filter { it.isCompleted }
                .sumOf { it.energyCost }

            remainingEnergy = totalEnergy - usedEnergy
            planActivities = updatedList

            if (withBreak.isNotEmpty()) {
                onBreakNeeded(withBreak.first().id)
            } else {
                onBreakNeeded(null)
            }
        }
    }

    // veiklai priskirtos pertraukos laikmačio inicijavimas
    fun startBreakTimer(planActivityId: Int, onDone: () -> Unit){
        viewModelScope.launch {
            val existing = breakRepository.getBreak(planActivityId)

            if(existing != null){
                val now = System.currentTimeMillis()
                val end = now + existing.durationMinutes * 60 * 1000L

                breakRepository.saveBreak(
                    planActivityId = existing.planActivityId,
                    durationMinutes = existing.durationMinutes,
                    startTime = now,
                    endTime = end,
                    isCompleted = false
                )
            }

            onDone()
        }
    }

    // einamos pertraukos ID grąžinimas
    fun getRunningBreakActivityId(): Int?{
        return planActivities
            .firstOrNull{ activity ->
                val hasBreakStarted = (activity.startTime ?: 0L) > 0L
                val breakNotCompleted = activity.breakIsCompleted == false
                hasBreakStarted && breakNotCompleted && !activity.isCompleted
            }
            ?.id
    }

    // veiklos su priskirta pertrauka užbaigimas
    fun completeAfterBreak(planActivityId: Int, onDone: () -> Unit){
        viewModelScope.launch {

            val existing = breakRepository.getBreak(planActivityId)

            if(existing != null) {
                breakRepository.saveBreak(
                    planActivityId = existing.planActivityId,
                    durationMinutes = existing.durationMinutes,
                    startTime = existing.startTime,
                    endTime = System.currentTimeMillis(),
                    isCompleted = true
                )
            }

            val completionTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            planActivityRepository.completeActivity(planActivityId, completionTime)

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val updatedList = planActivityRepository.getPlanActivitiesWithBreaks(today)

            val usedEnergy = updatedList
                .filter {it.isCompleted}
                .sumOf {it.energyCost}

            remainingEnergy = totalEnergy - usedEnergy
            planActivities = updatedList

            onDone()
        }
    }

    // visų plano veiklų atlikimo tikrinimas
    fun areAllActivitiesCompleted(): Boolean {
        return planActivities.isNotEmpty() && planActivities.all {it.isCompleted}
    }
}