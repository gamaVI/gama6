package com.example.gama6mobileapp.ui.add_location

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gama6mobileapp.databinding.FragmentAddLocationBinding
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


}