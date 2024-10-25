package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import timber.log.Timber

class ElectionPreparednessApplication: Application() {
    val electionsRepository: ElectionsRepository
        get() = ServiceLocator.provideElectionsRepository(this)

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}