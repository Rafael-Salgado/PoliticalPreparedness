package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionsLocalDataSource
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by lazy {
        val activity = requireNotNull(this.activity)
        val database = ElectionDatabase.getSavedDatabaseInstance(activity)
        val viewModelFactory = VoterInfoViewModelFactory(
                ElectionsLocalDataSource(database.electionDao),
                activity.application
        )
        ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val electionId = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val division =  VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision
        viewModel.getVoterInfo(division, electionId)

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        binding.buttonSaved.setOnClickListener {
            viewModel.followOrUnfollowElection()
        }

        return binding.root
    }


}