package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionsRepository

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(private val electionsRepository: ElectionsRepository, private val savedStateHandle: SavedStateHandle): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (ElectionsViewModel(electionsRepository, savedStateHandle) as T)
}