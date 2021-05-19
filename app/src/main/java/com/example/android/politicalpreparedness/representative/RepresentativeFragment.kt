package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {

    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionLocation =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
                if (permission) {
                    getLocation()
                } else {
                    Snackbar.make(
                            binding.root,
                            R.string.location_required_error,
                            Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        ArrayAdapter.createFromResource(
                requireContext(),
                R.array.states,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.state.adapter = adapter
        }

        binding.listRepresentatives.adapter = RepresentativeListAdapter()

        binding.buttonLocation.setOnClickListener {
            getLocation()
        }
        binding.buttonSearch.setOnClickListener {
            if (validateAddressFields()) {
               val address = getValuesFromFields()
                viewModel.getRepresentatives(address)
            }
            hideKeyboard()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        val address = getValuesFromFields()
        viewModel.setAddress(address)
    }

    private fun validateAddressFields(): Boolean {
        val errorMessage = requireContext().getString(R.string.error_message)
        var withoutError = true
        if (binding.addressLine1.text.toString().isEmpty()) {
            binding.addressLine1.error = errorMessage
            withoutError = false
        }
        if (binding.addressLine2.text.toString().isEmpty()) {
            binding.addressLine2.error = errorMessage
            withoutError = false
        }
        if (binding.city.text.toString().isEmpty()) {
            binding.city.error = errorMessage
            withoutError = false
        }
        if (binding.zip.text.toString().isEmpty()) {
            binding.zip.error = errorMessage
            withoutError = false
        }
        return withoutError
    }

    private fun getValuesFromFields(): Address {
        val line1 = binding.addressLine1.text.toString()
        val line2 = binding.addressLine2.text.toString()
        val city = binding.city.text.toString()
        val zip = binding.zip.text.toString()
        val state = binding.state.selectedItem.toString()

        return Address(line1,line2,city,state,zip)
    }

    private fun clearErrorMessages() {
        binding.addressLine1.setError(null, null)
        binding.addressLine2.setError(null, null)
        binding.city.setError(null, null)
        binding.zip.setError(null, null)
    }

    private fun getLocation() {
        if ((ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            permissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val address = geoCodeLocation(location)
                viewModel.getRepresentatives(address)
                clearErrorMessages()
            }
        }

    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}