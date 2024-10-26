package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.android.politicalpreparedness.repository.ElectionsRepository

class RepresentativeModelFactory(
    private val electionsRepository: ElectionsRepository,
    private val application: Application
) : AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when {
            modelClass.isAssignableFrom(RepresentativeViewModel::class.java) -> {
                RepresentativeViewModel(electionsRepository, application, handle) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}