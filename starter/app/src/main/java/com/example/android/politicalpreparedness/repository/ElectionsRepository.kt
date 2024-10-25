package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.network.models.asDatabaseModel
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {
    val elections: LiveData<List<Election>> = database.electionDao.getAllElections().map {
        it
    }

    val savedElections: LiveData<List<Election>> = database.followedElectionsDao.getFollowedElections()


    suspend fun getElectionByIdSync(id: Int): Election? {
        return withContext(Dispatchers.IO) {
            database.electionDao.getElectionSync(id)
        }
    }
    suspend fun getFollowedElectionByIdSync(id: Int): Int? {
        return withContext(Dispatchers.IO) {
            database.followedElectionsDao.getElectionSync(id)
        }
    }

    suspend fun insertFollowedElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.followedElectionsDao.insertFollowedElection(id)
        }
    }

    suspend fun deleteFollowedElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.followedElectionsDao.deleteFollowedElection(id)
        }
    }

    suspend fun getRepresentatives(address: String): List<Representative> {
        return withContext(Dispatchers.IO) {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address)
            offices.flatMap { office ->
                office.getRepresentatives(officials)
            }
        }
    }

    suspend fun getVoterInfo(id: Int, address: String): VoterInfoResponse {
        return withContext(Dispatchers.IO) {
            CivicsApi.retrofitService.getVoterInfo(address, id)
        }
    }

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val electionsResults = CivicsApi.retrofitService.getElections()
            database.electionDao.insertAll(*electionsResults.asDatabaseModel())
        }
    }


}