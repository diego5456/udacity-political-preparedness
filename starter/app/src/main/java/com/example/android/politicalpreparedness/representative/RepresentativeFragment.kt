package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Address
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.ElectionPreparednessApplication
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.utils.checkPermission
import com.example.android.politicalpreparedness.utils.createPermissionDeniedSnackbar
import com.example.android.politicalpreparedness.utils.locationPermissions
import com.example.android.politicalpreparedness.utils.permissionRequests
import timber.log.Timber

class DetailFragment : Fragment() {
    private var locationAddress: Address? = null
    private lateinit var binding: FragmentRepresentativeBinding
    private var representativeAdapter: RepresentativeListAdapter? = null
    private var recyclerViewState: Parcelable? = null
    private var locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            createPermissionDeniedSnackbar(this, locationPermissions.toastMessage)
        } else {
            viewModel.getLocationAndAddress(requireContext())
        }
    }

    private val viewModel by activityViewModels<RepresentativeViewModel> {
        RepresentativeModelFactory(
            (requireContext().applicationContext as ElectionPreparednessApplication).electionsRepository,
            requireActivity().application,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        formListener()
        representativeAdapter = RepresentativeListAdapter(RepresentativeListener {
            Log.i("Representative", "Clicked")
        })
        binding.representativeRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.representativeRecycler.adapter = representativeAdapter

        binding.buttonLocation.setOnClickListener {
            if (checkPermission(this, locationPermissions)) {
                viewModel.getLocationAndAddress(requireContext())
                binding.addressLine1.setText(this.locationAddress?.thoroughfare)
            } else {
                permissionRequests(this, locationPermissionLauncher, locationPermissions)
            }
        }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.getRepresentatives()
        }

        savedInstanceState?.let {
            it.getInt("motionState").takeIf {
                it != 0
            }?.let {
                binding.motionLayout.transitionToState(it)
            }
            recyclerViewState = it.getParcelable("recyclerState")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.representatives.observe(viewLifecycleOwner) { representative ->
            representativeAdapter?.submitList(representative)
        }
    }

    private fun formListener() {
        val fields = listOf(
            binding.addressLine1,
            binding.city,
            binding.zip
        )

        val formWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                checkForCompletion()
            }
        }

        fields.forEach {
            it.addTextChangedListener(formWatcher)
        }

        checkForCompletion()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("Representative", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putInt("spinnerSelection", binding.state.selectedItemPosition)
        outState.putInt("motionState", binding.motionLayout.currentState)

        recyclerViewState = binding.representativeRecycler.layoutManager?.onSaveInstanceState()
        outState.putParcelable("recyclerState", recyclerViewState)

        outState.putString("addressLine1", binding.addressLine1.text.toString())
        outState.putString("addressLine1", binding.addressLine2.text.toString())
        outState.putString("city", binding.city.text.toString())
        outState.putString("zip", binding.zip.text.toString())
        outState.putInt("spinnerSelection", binding.state.selectedItemPosition)
    }

    override fun onPause() {
        Log.i("Representative", "onPause")
        super.onPause()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Timber.i("onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
        recyclerViewState?.let {
            binding.representativeRecycler.layoutManager?.onRestoreInstanceState(it)
        }

        savedInstanceState?.let {
            binding.addressLine1.setText(it.getString("addressLine1", ""))
            binding.addressLine2.setText(it.getString("addressLine2", ""))
            binding.city.setText(it.getString("city", ""))
            binding.zip.setText(it.getString("zip", ""))
            binding.state.setSelection(it.getInt("spinnerSelection", 0))
        }

    }

    private fun checkForCompletion(): Boolean {
        val isComplete = viewModel.addressLine1.value?.isNotEmpty() == true &&
                viewModel.city.value?.isNotEmpty() == true &&
                viewModel.zip.value?.isNotEmpty() == true
        binding.buttonSearch.isEnabled = isComplete
        return isComplete
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}