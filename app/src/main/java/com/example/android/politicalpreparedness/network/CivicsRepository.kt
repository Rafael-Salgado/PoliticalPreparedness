package com.example.android.politicalpreparedness.network

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.Result
import com.example.android.politicalpreparedness.network.models.Election

interface CivicsRepository {

    suspend fun refreshElections()

    fun observeElections(): LiveData<Result<List<Election>>>

    suspend fun deleteAllElections()

    suspend fun deleteElections(id: Int)

}