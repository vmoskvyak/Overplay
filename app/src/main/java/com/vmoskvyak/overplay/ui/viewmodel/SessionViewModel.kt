package com.vmoskvyak.overplay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vmoskvyak.overplay.manager.SessionManager
import com.vmoskvyak.overplay.module.GyroscopeSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    @GyroscopeSensor gyroscopeZRotationFlow: Flow<Float>,
) : ViewModel() {

    private var sessionCount: Int = sessionManager.sessionCount
        set(value) {
            field = value
            updateState()
        }

    private var rotatePosition: RotatePosition = RotatePosition.DEFAULT
        set(value) {
            field = value
            updateState()
        }

    private val state
        get() = SessionUIState(
            sessionCount = sessionCount,
            rotatePosition = rotatePosition,
        )

    private val _sessionUiState = MutableStateFlow(state)
    val sessionUiState = _sessionUiState.asStateFlow()

    init {
        viewModelScope.launch {
            gyroscopeZRotationFlow.collect { zRotation ->
                rotatePosition = when {
                    zRotation > ROTATION_ANGLE -> RotatePosition.LEFT
                    zRotation < -ROTATION_ANGLE -> RotatePosition.RIGHT
                    else -> RotatePosition.DEFAULT
                }
            }
        }
    }

    private fun updateState() {
        _sessionUiState.update {
            state
        }
    }

    fun onAppForegrounded() {
        sessionManager.onAppForegrounded()
        sessionCount = sessionManager.sessionCount
    }

    fun onAppBackgrounded() {
        sessionManager.onAppBackgrounded()
    }

    companion object {
        const val ROTATION_ANGLE = 30
    }
}

data class SessionUIState(
    val sessionCount: Int,
    val rotatePosition: RotatePosition,
)

enum class RotatePosition {
    DEFAULT, LEFT, RIGHT
}
