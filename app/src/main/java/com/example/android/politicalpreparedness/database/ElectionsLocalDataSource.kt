package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ElectionsLocalDataSource internal constructor(
    private val electionDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionsDataSource{

    override fun getElections(): LiveData<Result<List<Election>>> {
        return electionDao.getElections().map { electionList->
            Result.Success(electionList)
        }
    }

    override suspend fun getElectionById(id: Int): Result<Election> = withContext(ioDispatcher) {
       try {
           val election = electionDao.getElectionById(id)
           if (election != null) {
               return@withContext Result.Success(election)
           } else {
               return@withContext Result.Error(Exception("Election not found!"))
           }
       }catch (e: Exception){
           return@withContext Result.Error(e)
       }


    }

    override suspend fun saveElections(vararg elections: Election) = withContext(ioDispatcher) {
        electionDao.insertAll(*elections)
    }

    override suspend fun deleteElection(id: Int): Unit = withContext(ioDispatcher) {
        electionDao.deleteElectionById(id)
    }

    override suspend fun deleteAllElections() = withContext(ioDispatcher) {
        electionDao.clearElections()
    }
}