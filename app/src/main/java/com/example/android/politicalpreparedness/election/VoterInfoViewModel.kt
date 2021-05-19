package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionsDataSource
import com.example.android.politicalpreparedness.database.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.database.Result
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.getStateNameFromCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class VoterInfoViewModel(private val dataSource: ElectionsLocalDataSource, application: Application) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val followText = application.getString(R.string.follow_election)
    private val unfollowText = application.getString(R.string.unfollow_election)

    private val _buttonSavedText = MutableLiveData<String>()
    val buttonSavedText: LiveData<String>
        get() = _buttonSavedText

    private suspend fun getSavedElection(id: Int) {
        val election = filterElection(dataSource.getElectionById(id))
        if (election != null) {
            _buttonSavedText.value = unfollowText
        } else {
            _buttonSavedText.value = followText
        }
    }

    private fun filterElection(electionResult: Result<Election>): Election? {
        return if (electionResult is Result.Success) {
            electionResult.data
        } else {
            null
        }
    }


    fun getVoterInfo(division: Division, electionId: Int) {

        viewModelScope.launch {
            getSavedElection(electionId)
            try {
                val state = getStateNameFromCode(division.state)
                val response = CivicsApi.retrofitService.getVoterInfoAsync(state, electionId)
                _voterInfo.value = response.await()
            } catch (e: Exception) {
                Log.e("ELECTION_API", e.toString())
            }
        }
    }

    fun followOrUnfollowElection() {
        if (_buttonSavedText.value.equals(unfollowText)) {
            deleteElection()
            _buttonSavedText.value = followText
        } else {
            saveElection()
            _buttonSavedText.value = unfollowText
        }
    }

    private fun saveElection() {
        val election = _voterInfo.value?.election
        if (election != null) {
            viewModelScope.launch {
                dataSource.saveElections(election)

            }
        }
    }

    private fun deleteElection() {
        val election = _voterInfo.value?.election
        if (election != null) {
            viewModelScope.launch {
                dataSource.deleteElection(election.id)
            }
        }
    }

}