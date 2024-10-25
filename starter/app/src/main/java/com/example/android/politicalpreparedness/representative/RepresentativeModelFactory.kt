package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.ElectionPreparednessApplication
import com.example.android.politicalpreparedness.repository.ElectionsRepository

class RepresentativeModelFactory(private val electionsRepository: ElectionsRepository, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        (RepresentativeViewModel(electionsRepository, application) as T)

}