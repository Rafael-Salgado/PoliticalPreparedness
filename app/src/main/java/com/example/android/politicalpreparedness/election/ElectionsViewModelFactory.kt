package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.network.DefaultCivicsRepository


class ElectionsViewModelFactory(
        private val defaultCivicsRepository: DefaultCivicsRepository,
        private val electionSavedDataSource: ElectionsLocalDataSource
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ElectionsViewModel(defaultCivicsRepository, electionSavedDataSource) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}