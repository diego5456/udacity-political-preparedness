package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.LocationUtils
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val electionsRepository: ElectionsRepository, private val application: Application) : ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _representatives =  MutableLiveData<List<Representative>>()
    val representatives:  LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<String>()
    val address: MutableLiveData<String>
        get() = _address

    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val statePosition = MutableLiveData<Int>()
    val zip = MutableLiveData<String>()
    val response = MutableLiveData<List<Representative>>()


    //TODO: Create function to fetch representatives from API from a provided address

    fun getRepresentatives() {
        viewModelScope.launch {
            try {
                getAddressFromFields()
                _representatives.value =
                    address.value?.let { electionsRepository.getRepresentatives(it) }
            } catch (e: Exception) {
                Log.e("RepresentativeViewModel", "Error fetching representatives: ${e.message}")
                Toast.makeText(application, "Error fetching representatives", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun getLocationAndAddress(context: Context) {
        viewModelScope.launch {
            val address = LocationUtils.getLocationAndAddress(context)
            if (address != null) {
                Log.i("RepresentativeFragment", "getLocationAndAddress: $address")
                val locationAddress = address

                val thoroughfare = address.thoroughfare ?: ""
                val subThoroughfare = address.subThoroughfare ?: ""
                val addressCity = address.locality ?: ""
                val addressState = address.adminArea ?: ""
                val addressZip = address.postalCode ?: ""


                addressLine1.value = thoroughfare
                addressLine2.value = subThoroughfare
                city.value = addressCity
                zip.value = addressZip
                statePosition.value = getStatePosition(addressState)
            }
        }
    }

    private fun getStatePosition(state: String): Int {
        val states = application.resources.getStringArray(R.array.states)
        return states.indexOf(state).takeIf { it >= 0 } ?: 0
    }

    //TODO: Create function to get address from individual fields
    private fun getAddressFromFields(): String {
        val addressLine1 = addressLine1.value
        val addressLine2 = addressLine2.value
        val city = city.value
        val state = state.value
        val zip = zip.value
        address.value =  "$addressLine1 $addressLine2 $city $state $zip"
        return ""
    }

}
