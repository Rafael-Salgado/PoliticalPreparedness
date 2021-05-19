package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getSavedDatabaseInstance
import com.example.android.politicalpreparedness.database.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.DefaultCivicsRepository
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val database = getInstance(activity)
        val electionsFollowedDatabase = getSavedDatabaseInstance(activity)
        val localDataSource = ElectionsLocalDataSource(database.electionDao)
        val repository = DefaultCivicsRepository(localDataSource)
        val viewModelFactory = ElectionsViewModelFactory(
                repository,
                ElectionsLocalDataSource(electionsFollowedDatabase.electionDao)
        )
        ViewModelProvider(this,viewModelFactory).get(ElectionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.upcomingElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
            viewModel.displayVoterInfo(it)
        })
        binding.savedElectionRecycler.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.displayVoterInfo(it)
        })

        viewModel.navigateToSelectedElection.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                        ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it.id,it.division)
                )
                viewModel.displayVoterInfoCompleted()
            }

        })
        return binding.root
    }

}