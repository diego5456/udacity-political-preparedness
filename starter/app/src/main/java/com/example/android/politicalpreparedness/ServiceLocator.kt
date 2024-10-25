package com.example.android.politicalpreparedness

import android.content.Context
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.repository.ElectionsRepository

object ServiceLocator {

    private val lock = Any()
    private var database: ElectionDatabase? = null

    @Volatile
    var electionsRepository: ElectionsRepository? = null

    fun provideElectionsRepository(context: Context): ElectionsRepository {
        synchronized(this) {
            return electionsRepository?: createElectionsRepository(context)
        }
    }

    private fun createElectionsRepository(context: Context): ElectionsRepository {
        val newRepo = ElectionsRepository(createElectionsDatabase(context))
        electionsRepository = newRepo
        return newRepo
    }

    private fun createElectionsDatabase(context: Context): ElectionDatabase{
        val result = ElectionDatabase.getInstance(context)
        database = result
        return result
    }
}