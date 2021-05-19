package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    @Query("select * from election_table")
    fun  getElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :electionId")
    suspend fun getElectionById(electionId: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :electionId")
    suspend fun deleteElectionById(electionId: Int): Int

    @Query("DELETE FROM election_table")
    suspend fun clearElections()
}