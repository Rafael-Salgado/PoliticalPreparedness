package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election


interface ElectionsDataSource {

    fun getElections(): LiveData<Result<List<Election>>>

    suspend fun getElectionById(id: Int): Result<Election>

    suspend fun saveElections(vararg elections: Election)

    suspend fun deleteElection(id: Int)

    suspend fun deleteAllElections()

}