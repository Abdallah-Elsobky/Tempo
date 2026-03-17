package iti.student.finalproject.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import iti.student.finalproject.domain.mapper.AlertMapper.entityToModel
import iti.student.finalproject.domain.mapper.AlertMapper.modelToEntity
import iti.student.finalproject.domain.model.AlertModel
import iti.student.finalproject.domain.repository.AlertRepository
import iti.student.finalproject.worker.AlertScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel(
    private val repo: AlertRepository,
    private val alertScheduler: AlertScheduler
) : ViewModel() {
    private val _alerts = MutableStateFlow<List<AlertModel>>(emptyList())
    val alerts: StateFlow<List<AlertModel>> = _alerts

    init {
        viewModelScope.launch {
            var firstLoad = true
            repo.getAlerts()
                .collect { entities ->
                    val models = entityToModel(entities)
                    _alerts.value = models
                    if (firstLoad) {
                        firstLoad = false
                        val now = System.currentTimeMillis()
                        models.forEach { model ->
                            if (model.endDate < now) {
                                alertScheduler.cancelAlert(model.id)
                                repo.deleteAlertById(model.id)
                            } else {
                                alertScheduler.scheduleAlert(model)
                            }
                        }
                    }
                }
        }
    }

    fun insertAlert(alert: AlertModel) {
        viewModelScope.launch {
            val id = repo.insertAlert(modelToEntity(alert))
            val alertWithId = alert.copy(id = id.toInt())
            alertScheduler.scheduleAlert(alertWithId)
        }
    }

    fun deleteAlert(alert: AlertModel) {
        viewModelScope.launch {
            alertScheduler.cancelAlert(alert.id)
            repo.deleteAlert(modelToEntity(alert))
        }
    }
}

class AlertViewModelFactory(
    private val repository: AlertRepository,
    private val alertScheduler: AlertScheduler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(repository, alertScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}