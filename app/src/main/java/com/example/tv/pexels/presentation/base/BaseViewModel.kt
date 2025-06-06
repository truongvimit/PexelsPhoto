package com.example.tv.pexels.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    object ConstantBaseViewModel {
        const val TIME_ANIMATION_NAVIGATE = 300
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(ConstantBaseViewModel.TIME_ANIMATION_NAVIGATE.toLong())
            onInit() // Gọi hàm abstract để ViewModel con override logic khởi tạo
        }
    }

    protected abstract fun onInit() // Bắt buộc ViewModel con phải override
}
