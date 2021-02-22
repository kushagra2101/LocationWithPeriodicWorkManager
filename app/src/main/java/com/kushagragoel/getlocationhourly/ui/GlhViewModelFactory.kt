package com.kushagragoel.getlocationhourly.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GlhViewModelFactory(
    private val applicationInstance: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationFetchViewModel::class.java)) {
            return LocationFetchViewModel(applicationInstance) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}