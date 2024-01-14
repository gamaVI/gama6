package com.example.gama6mobileapp.ui.add_location

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.gama6mobileapp.appClass.Gama6Application
import com.example.gama6mobileapp.databinding.FragmentAddLocationBinding
import com.example.gama6mobileapp.model.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class AddLocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private var _binding: FragmentAddLocationBinding? = null
    private var mGoogleMap: GoogleMap? = null
    private var mapView: MapView? = null
    private var currentMarker: Marker? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private fun generateRandomNumber(minCars: Int?, maxCars: Int?): Int {
        return if (minCars != null && maxCars != null) {
            (minCars..maxCars).random()
        } else {
            0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabSearchLocation.setOnClickListener {
            val address = binding.addLocationTilLocationName.editText?.text.toString()
            geocodeAddress(address)
        }

        // Initialize MapView
        mapView = binding.mapView.apply {
            onCreate(savedInstanceState)
            getMapAsync(this@AddLocationFragment)
        }

        setupTimePicker()

        binding.btnSaveLocation.setOnClickListener {
            val locationName = binding.addLocationTilLocationName.editText?.text.toString().trim()
            val minCarsString =
                binding.addLocationEtRandomGenerationRangeMin.text?.toString()?.trim()
            val maxCarsString =
                binding.addLocationEtRandomGenerationRangeMax.text?.toString()?.trim()

            var minCars = minCarsString?.toIntOrNull()
            val maxCars = maxCarsString?.toIntOrNull()

            val locationUpdateFrequencyHours = binding.addLocationNpUpdateFrequencyHours.value
            val locationUpdateFrequencyMinutes = binding.addLocationNpUpdateFrequencyMinutes.value
            val locationUpdateFrequencySeconds = binding.addLocationNpUpdateFrequencySeconds.value
            val locationUpdateFrequency =
                locationUpdateFrequencyHours * 3600 + locationUpdateFrequencyMinutes * 60 + locationUpdateFrequencySeconds

            val locationLatitude = currentMarker?.position?.latitude
            val locationLongitude = currentMarker?.position?.longitude
            val locationSimulation = binding.switchSimulateLocation.isChecked

            if (minCars != null && minCars < 0) minCars = 0
            if (validateData(
                    locationName,
                    minCars,
                    maxCars,
                    locationUpdateFrequency,
                    locationLatitude,
                    locationLongitude
                )
            ) {
                val numCars = generateRandomNumber(minCars, maxCars)
                val location = Location(
                    name = locationName,
                    minCars = minCars ?: 0,  // Defaulting to 0 if null
                    maxCars = maxCars ?: 0,  // Defaulting to 0 if null
                    updateFrequencySeconds = locationUpdateFrequency,
                    latitude = locationLatitude,
                    longitude = locationLongitude,
                    simulation = locationSimulation,
                    numCars = numCars
                )
                println(location)
                binding.addLocationTilLocationName.editText?.text?.clear()
                binding.addLocationEtRandomGenerationRangeMin.text?.clear()
                binding.addLocationEtRandomGenerationRangeMax.text?.clear()
                binding.addLocationNpUpdateFrequencyHours.value = 0
                binding.addLocationNpUpdateFrequencyMinutes.value = 0
                binding.addLocationNpUpdateFrequencySeconds.value = 0
                binding.switchSimulateLocation.isChecked = false
                Toast.makeText(context, "Location saved!", Toast.LENGTH_LONG).show()
                val app = Gama6Application.instance
                if(app.upsertLocation(location)) {
                    Toast.makeText(context, "Location saved!", Toast.LENGTH_LONG).show()
                    app.locations.add(location)
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(context, "Location not saved!", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(context, "Invalid data...", Toast.LENGTH_LONG).show()
            }
        }
        return root
    }

    override fun onMapReady(p0: GoogleMap) {
        mGoogleMap = p0
        val centerOfSlovenia = LatLng(46.1512, 14.9955)
        mGoogleMap?.moveCamera(
            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                centerOfSlovenia,
                7f
            )
        )
        mGoogleMap?.setOnMapClickListener(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView?.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onMapClick(p0: LatLng) {
        reverseGeocodeLatLng(p0)
    }

    private fun geocodeAddress(address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
                val addresses = geocoder?.getFromLocationName(address, 1)
                if (!addresses.isNullOrEmpty()) {
                    val location = addresses[0]
                    val latLng = LatLng(location.latitude, location.longitude)
                    withContext(Dispatchers.Main) {
                        updateMapLocation(latLng)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle exceptions
            }
        }
    }

    private fun reverseGeocodeLatLng(latlng: LatLng) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geocoder = context?.let { Geocoder(it, Locale.getDefault()) }
                val addresses = geocoder?.getFromLocation(latlng.latitude, latlng.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0)
                withContext(Dispatchers.Main) {
                    updateMapLocation(latlng)
                    address?.let { binding.addLocationTilLocationName.editText?.setText(it) }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                // Handle exceptions
            }
        }
    }

    private fun updateMapLocation(latLng: LatLng) {
        currentMarker?.remove() // Remove the existing marker, if any
        currentMarker = mGoogleMap?.addMarker(MarkerOptions().position(latLng))
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7f))
    }

    private fun setupTimePicker() {
        val hoursPicker = binding.addLocationNpUpdateFrequencyHours
        val minutesPicker = binding.addLocationNpUpdateFrequencyMinutes
        val secondsPicker = binding.addLocationNpUpdateFrequencySeconds

        hoursPicker.minValue = 0
        hoursPicker.maxValue = 23
        hoursPicker.value = 0

        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59
        minutesPicker.value = 0

        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59
        secondsPicker.value = 0

    }

    private fun validateData(
        locationName: String,
        minCars: Int?,
        maxCars: Int?,
        locationUpdateFrequency: Int,
        locationLatitude: Double?,
        locationLongitude: Double?
    ): Boolean {
        if (locationName.isEmpty()) {
            return false
        }

        if ((minCars != null) && (maxCars != null) && (minCars > maxCars) && (minCars < 0)) {
            return false
        }

        if (locationUpdateFrequency == 0) {
            return false
        }

        if (locationLatitude == null || locationLongitude == null) {
            return false
        }

        return true
    }


}