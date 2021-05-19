package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RepresentativeViewModel : ViewModel() {

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address
    private val _repResponse = MutableLiveData<RepresentativeResponse?>()
    val repResponse: LiveData<RepresentativeResponse?>
        get() = _repResponse

    fun getRepresentatives(address: Address) {
        _address.value = address
        viewModelScope.launch {
            if (_address.value != null) {
               _repResponse.value = getResponse(_address.value!!.toFormattedString())
            }
        }
    }

    private suspend fun getResponse(address: String): RepresentativeResponse? {
        var response: RepresentativeResponse? = null
        try {
            withContext(Dispatchers.IO) {
                response = CivicsApi.retrofitService.getRepresentativesAsync(address).await()
                return@withContext response
            }
        } catch (e: Exception) {
            Log.e("CIVIC_API", e.toString())
        }
        return response
    }

    fun setAddress(address: Address){
        _address.value = address
    }


    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
}
