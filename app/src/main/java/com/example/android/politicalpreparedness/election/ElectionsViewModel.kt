package com.example.android.politicalpreparedness.election


import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.network.DefaultCivicsRepository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.database.Result


class ElectionsViewModel(
        private val defaultCivicsRepository: DefaultCivicsRepository,
        electionSavedDataSource: ElectionsLocalDataSource
) : ViewModel() {

    private val _electionsList = MutableLiveData<List<Election>>()
    val electionsList: LiveData<List<Election>>
        get() = _electionsList
    private var electionsListLiveData: LiveData<List<Election>>
    private val electionsListObserver = Observer<List<Election>> {
        _electionsList.value = it
    }
    private val _electionsFollowedList = MutableLiveData<List<Election>>()
    val electionsFollowedList: LiveData<List<Election>>
        get() = _electionsFollowedList
    private var electionsFollowedLiveData: LiveData<List<Election>>
    private val electionsFollowedObserver = Observer<List<Election>> {
        _electionsFollowedList.value = it
    }
    private val _navigateToSelectedElection = MutableLiveData<Election?>()
    val navigateToSelectedElection: LiveData<Election?>
        get() = _navigateToSelectedElection

    init {
        electionsListLiveData = defaultCivicsRepository.observeElections().switchMap {
            filterElections(it)
        }
        electionsListLiveData.observeForever(electionsListObserver)
        electionsFollowedLiveData = electionSavedDataSource.getElections().switchMap {
            filterElections(it)
        }
        electionsFollowedLiveData.observeForever(electionsFollowedObserver)
        viewModelScope.launch {
            defaultCivicsRepository.refreshElections()
        }

    }

    override fun onCleared() {
        super.onCleared()
        electionsListLiveData.removeObserver(electionsListObserver)
        electionsFollowedLiveData.removeObserver(electionsFollowedObserver)
    }

    private fun filterElections(electionsResult: Result<List<Election>>): LiveData<List<Election>> {
        val result = MutableLiveData<List<Election>>()
        if (electionsResult is Result.Success) {
            result.value = electionsResult.data!!
        } else {
            result.value = emptyList()
        }
        return result
    }

    fun displayVoterInfo(election: Election) {
        _navigateToSelectedElection.value = election
    }

    fun displayVoterInfoCompleted() {
        _navigateToSelectedElection.value = null
    }

}