package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val electionsRepository: ElectionsRepository) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _followedElection = MutableLiveData<Int>()
    val followedElection: LiveData<Int>
        get() = _followedElection
    private var _election = MutableLiveData<Election?>()
    val election: LiveData<Election?>
        get() = _election
    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address
    private val _electionId = MutableLiveData<Int>()
    val electionId: LiveData<Int>
        get() = _electionId
    private val _division = MutableLiveData<Division>()
    val division: LiveData<Division>
        get() = _division
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _navigateBackToElections = MutableLiveData<Boolean>()
    val navigateBackToElections: LiveData<Boolean>
        get() = _navigateBackToElections

    fun doneNavigating() {
        _navigateBackToElections.value = false
    }


    //TODO: Add var and methods to populate voter info

    private suspend fun getVoterInfo() {
        _voterInfo.value = electionId.value?.let {
            address.value?.let { it1 ->
                electionsRepository.getVoterInfo(
                    it,
                    it1
                )
            }
        }

        Log.i("VoterInfoViewModel", "getVoterInfo: ${voterInfo.value}")
    }

    fun start(electionId: Int, division: Division, address: String) {
        _electionId.value = electionId
        _division.value = division
        _address.value = address

        viewModelScope.launch {
            try {
                _isLoading.value = true
                getVoterInfo()

                // Fetch the election synchronously
                val fetchElection = electionsRepository.getElectionByIdSync(electionId)
                if (fetchElection != null) {
                    _election.value = fetchElection
                } else {
                    Log.i("VoterInfoViewModel", "start: Election not found")
                }
                _followedElection.value =
                    electionsRepository.getFollowedElectionByIdSync(electionId)


            } catch (e: Exception) {
                Log.e("VoterInfoViewModel", "Error getting voter info: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFollow() {
        viewModelScope.launch {
            val followed = _followedElection.value
            if (followed == null) {
                electionsRepository.insertFollowedElection(electionId.value!!)
            } else {
                electionsRepository.deleteFollowedElection(electionId.value!!)
            }
            _navigateBackToElections.value = true
        }
    }
}