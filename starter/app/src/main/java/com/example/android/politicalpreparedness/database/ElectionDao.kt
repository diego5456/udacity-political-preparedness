package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionData

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll (vararg election: Election)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateElection(election: Election)

    //TODO: Add select all election query
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElection(id: Int): LiveData<Election>

    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun getElectionSync(id: Int): Election?

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    fun deleteElection(id: Int)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    fun deleteAll()


}