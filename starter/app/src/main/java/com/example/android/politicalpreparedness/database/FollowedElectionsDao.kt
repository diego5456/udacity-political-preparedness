package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface FollowedElectionsDao {
    @Query("SELECT * FROM followed_elections CROSS JOIN election_table ON followed_elections.id = election_table.id")
    fun getFollowedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM followed_elections WHERE id = :id")
    fun getElection(id: Int): LiveData<Int>

    @Query("SELECT * FROM followed_elections WHERE id = :id")
    suspend fun getElectionSync(id: Int): Int?

    @Query("INSERT INTO followed_elections (id) VALUES (:id)")
    fun insertFollowedElection(id: Int)

    @Query("DELETE FROM followed_elections WHERE id = :id")
    fun deleteFollowedElection(id: Int)

}