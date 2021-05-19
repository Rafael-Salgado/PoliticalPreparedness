package com.example.android.politicalpreparedness.network

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.database.Result
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class DefaultCivicsRepository(
        private val electionsLocalDataSource: ElectionsLocalDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CivicsRepository {

    override suspend fun refreshElections() {
        withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.getElectionsAsync()
                val electionsList = response.await().elections
                electionsLocalDataSource.saveElections(*electionsList.toTypedArray())
            } catch (e: Exception) {
                Log.e("ELECTION_API", e.toString())
            }

        }
    }

    override fun observeElections(): LiveData<Result<List<Election>>> {
        return electionsLocalDataSource.getElections()
    }

    override suspend fun deleteAllElections() {

    }

    override suspend fun deleteElections(id: Int) {

    }
}