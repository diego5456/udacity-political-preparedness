package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.ElectionPreparednessApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.utils.LocationUtils
import kotlinx.coroutines.launch

class VoterInfoFragment : Fragment() {
    private val infoViewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory((requireContext().applicationContext as ElectionPreparednessApplication).electionsRepository)
    }
    private lateinit var binding: FragmentVoterInfoBinding
    private var locationAddress: String? = null
    private val args: VoterInfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        val view = inflater.inflate(R.layout.fragment_voter_info, container, false)
        binding = FragmentVoterInfoBinding.bind(view).apply {
            viewModel = infoViewModel
        }
        binding.lifecycleOwner = this
        getLocationAndAddress()
        infoViewModel.followedElection.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.followButton.text = getString(R.string.unfollow_button)
            } else {
                binding.followButton.text = getString(R.string.follow_button)
            }
            binding.invalidateAll()
        })

        infoViewModel.navigateBackToElections.observe(
            viewLifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate == true) {
                    findNavController().navigate(R.id.action_voterInfoFragment_to_electionsFragment)
                    infoViewModel.doneNavigating()
                }
            })
        return view
    }


    private fun getLocationAndAddress() {
        lifecycleScope.launch {
            val address = LocationUtils.getLocationAndAddress(requireContext())
            if (address != null) {
                locationAddress = address.getAddressLine(0)// Display the address in a TextView or handle it as needed
                infoViewModel.start(args.argElectionId, args.argDivision, locationAddress!!)

            } else {
                // Handle the case where the address is null
                Log.e("VoterInfoFragment", "getLocationAndAddress: Address is null")
            }
        }
    }

}