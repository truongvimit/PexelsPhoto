package com.example.tv.pexels.presentation.ui.activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tv.pexels.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val application: Application,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<BaseState> = MutableStateFlow(BaseState())
    val stateFlow: StateFlow<BaseState> = _stateFlow.asStateFlow()
    private val notificationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun showDialogAuto(
        textRes: Int = R.string.something_wrong,
        type: TypeNotification = TypeNotification.NORMAL,
        duration: Long = 1000,
        formatArgs: Any? = null,
    ) {
        Log.d("showDialogAuto", textRes.toString())
        _stateFlow.update {
            it.copy(
                error = textRes,
                errorText = null,
                isShowDialogAuto = true,
                type = type,
                formatArgs = formatArgs
            )
        }
        notificationScope.launch {
            delay(duration) // 1 seconds
            _stateFlow.update {
                it.copy(
                    isShowDialogAuto = false,
                )
            }
        }
    }

    fun showDialogAuto(
        textString: String? = null,
        type: TypeNotification = TypeNotification.NORMAL,
        duration: Long = 1000,
        formatArgs: Any? = null,
    ) {
        _stateFlow.update {
            it.copy(
                error = null,
                errorText = textString,
                isShowDialogAuto = true,
                type = type,
                formatArgs = formatArgs
            )
        }
        notificationScope.launch {
            delay(duration) // 1 seconds
            _stateFlow.update {
                it.copy(
                    isShowDialogAuto = false,
                )
            }
        }
    }

    fun isLoading(value: Boolean) {
        _stateFlow.update {
            it.copy(
                isLoading = value
            )
        }
    }

    fun showDialogTryAgain() {
        _stateFlow.update {
            it.copy(
                isShowDialogTryAgain = true
            )
        }
    }
}

data class BaseState(
    val error: Int? = null,
    val errorText: String? = null,
    val isShowDialogAuto: Boolean = false,
    val isLoading: Boolean = false,
    val isShowDialogTryAgain: Boolean = false,
    val type: TypeNotification = TypeNotification.NORMAL,
    val formatArgs: Any? = null,
)

enum class TypeNotification {
    SUCCESS,
    NORMAL,
    FAIL
}