package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionData
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val electionsRepository: ElectionsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_ELECTIONS = "elections"
    }

    private val _elections = savedStateHandle.getLiveData<List<Election>>(KEY_ELECTIONS)
    val elections: LiveData<List<Election>> get() = _elections

    init {
        viewModelScope.launch {
            if (_elections.value == null || _elections.value!!.isEmpty()) {
                Log.d("ElectionsViewModel", "Refreshing elections from API")
                refreshElections()
            } else {
                Log.d("ElectionsViewModel", "Using cached elections")
            }
        }
    }

    private suspend fun refreshElections() {
        try {
            electionsRepository.refreshElections() // Fetch elections from API
            val electionsList = electionsRepository.elections.value // Get the current elections from repository
            if (electionsList != null && electionsList.isNotEmpty()) {
                _elections.value = electionsList // Update LiveData
                savedStateHandle[KEY_ELECTIONS] = electionsList // Save to SavedStateHandle
            }
        } catch (e: Exception) {
            Log.e("ElectionsViewModel", "Error refreshing elections: ${e.message}")
        }
    }

    // LiveData for upcoming elections
    val upcomingElections: LiveData<List<Election>> = electionsRepository.elections

    // LiveData for saved elections
    val savedElections: LiveData<List<Election>> = electionsRepository.savedElections

    // Add additional functions if needed to manage state or navigation
}