package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Address
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.ElectionPreparednessApplication
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.utils.checkPermission
import com.example.android.politicalpreparedness.utils.createPermissionDeniedSnackbar
import com.example.android.politicalpreparedness.utils.locationPermissions
import com.example.android.politicalpreparedness.utils.permissionRequests

class DetailFragment : Fragment() {
    private var locationAddress: Address? = null
    private lateinit var binding: FragmentRepresentativeBinding
    private var representativeAdapter: RepresentativeListAdapter? = null
    private var lastFocusedViewId: Int? = null
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
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        formListener()
        focusListener()
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
            clearFocus()
            viewModel.getRepresentatives()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.representatives.observe(viewLifecycleOwner) { representative ->
            representativeAdapter?.submitList(representative)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("addressLine1", binding.addressLine1.text.toString())
        outState.putString("addressLine2", binding.addressLine2.text.toString())
        outState.putString("city", binding.city.text.toString())
        outState.putString("zip", binding.zip.text.toString())
        outState.putInt("state", binding.state.selectedItemPosition)
        outState.putInt("motionState", binding.motionLayout.currentState)
        outState.putBoolean("isKeyboardOpen", isKeyboardOpen())
        outState.putInt("lastFocusedViewId", lastFocusedViewId ?: View.NO_ID)

        outState.putParcelableArrayList("representatives",
            viewModel.representatives.value?.let { ArrayList(it) })
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            viewModel.addressLine1.value = it.getString("addressLine1")
            viewModel.addressLine2.value = it.getString("addressLine2")
            viewModel.city.value = it.getString("city")
            viewModel.zip.value = it.getString("zip")
            viewModel.statePosition.value = it.getInt("state")
            viewModel.setRepresentatives(
                it.getParcelableArrayList("representatives") ?: emptyList()
            )
            val focusId = it.getInt("lastFocusedViewId")
            if (it.getBoolean("isKeyboardOpen") and (focusId != View.NO_ID)) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val targetView = view?.findViewById<EditText>(lastFocusedViewId!!)
                    targetView?.requestFocus()
                    showKeyboard()
                }, 100)
            } else {
                hideKeyboard()
            }
            savedInstanceState.getInt("motionState").let {
                binding.motionLayout.transitionToState(it)
            }
            checkForCompletion()
        }
    }

    private fun focusListener() {
        binding.addressLine1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) lastFocusedViewId = binding.addressLine1.id
        }
        binding.addressLine2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) lastFocusedViewId = binding.addressLine2.id
        }
        binding.city.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) lastFocusedViewId = binding.city.id
        }
        binding.zip.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) lastFocusedViewId = binding.zip.id
        }
    }
    fun clearFocus() {
        binding.addressLine1.clearFocus()
        binding.addressLine2.clearFocus()
        binding.city.clearFocus()
        binding.zip.clearFocus()
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

    private fun checkForCompletion(): Boolean {
        val isComplete = viewModel.addressLine1.value?.isNotEmpty() == true &&
                viewModel.city.value?.isNotEmpty() == true &&
                viewModel.zip.value?.isNotEmpty() == true
        binding.buttonSearch.isEnabled = isComplete
        return isComplete
    }

    private fun isKeyboardOpen(): Boolean {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isAcceptingText
    }

    private fun showKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}