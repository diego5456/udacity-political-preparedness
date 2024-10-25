package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.ElectionPreparednessApplication
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.utils.checkPermission
import com.example.android.politicalpreparedness.utils.createPermissionDeniedSnackbar
import com.example.android.politicalpreparedness.utils.locationPermissions
import com.example.android.politicalpreparedness.utils.permissionRequests

class ElectionsFragment : Fragment() {
    private var electionId: Int = 0
    private lateinit var division: Division

    private var locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            createPermissionDeniedSnackbar(this, locationPermissions.toastMessage)
        } else {
            navigateToVoterInfo(electionId, division)
        }
    }

    // TODO: Declare ViewModel
    private val viewModel by activityViewModels<ElectionsViewModel> {
        ElectionsViewModelFactory((requireContext().applicationContext as ElectionPreparednessApplication).electionsRepository, savedStateHandle = SavedStateHandle())
    }

    private var upcomingElectionListAdapter: ElectionListAdapter? = null
    private var savedElectionListAdapter: ElectionListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        upcomingElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            electionId = election.id
            division = election.division
            if (checkPermission(this, locationPermissions)) {
                navigateToVoterInfo(electionId, division)
            } else {
                permissionRequests(this, locationPermissionLauncher, locationPermissions)
            }
        })

        savedElectionListAdapter = ElectionListAdapter(ElectionListener { election ->
            electionId = election.id
            division = election.division
            if (checkPermission(this, locationPermissions)) {
                navigateToVoterInfo(electionId, division)
            } else {
                permissionRequests(this, locationPermissionLauncher, locationPermissions)
            }
        })

        binding.upcomingRecycler.adapter = upcomingElectionListAdapter
        binding.followedRecycler.adapter = savedElectionListAdapter

        return binding.root
    }


    private fun navigateToVoterInfo(id: Int, division: Division) {
        findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                id,
                division
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.upcomingElections.observe(viewLifecycleOwner) { elections ->
            upcomingElectionListAdapter?.submitList(elections)
        }
        viewModel.savedElections.observe(viewLifecycleOwner) { elections ->
            savedElectionListAdapter?.submitList(elections)
        }
    }

    // TODO: Refresh adapters when fragment loads
}