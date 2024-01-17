package com.example.gama6mobileapp.ui.my_locations

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gama6mobileapp.BuildConfig
import com.example.gama6mobileapp.R
import com.example.gama6mobileapp.adapter.RecycleViewAdapter
import com.example.gama6mobileapp.apiService.ApiService
import com.example.gama6mobileapp.apiService.ApiService.deleteLocation
import com.example.gama6mobileapp.appClass.Gama6Application
import com.example.gama6mobileapp.databinding.FragmentMyLocationsBinding
import com.example.gama6mobileapp.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MyLocationsFragment : Fragment(), RecycleViewAdapter.OnLocationLongClickListener {

    private var _binding: FragmentMyLocationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recycleViewAdapter: RecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        setUpRecyclerView()
        setUpSwipeRefreshLayout()
        fetchLocations()
        lifecycleScope.launch {
            val app = Gama6Application.instance
            while (isActive) {
                val locationsCopy = ArrayList(app.locations)
                if (locationsCopy.isNotEmpty()) {
                    updateLocationsIfNeeded(locationsCopy)
                }
                delay(5 * 1000) // Delay outside the forEachIndexed loop
            }
        }


        binding.fabAddLocation.setOnClickListener {
            findNavController().navigate(R.id.fromMyLocationsToAddLocation)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        fetchLocations()
    }

    private fun setUpRecyclerView() {
        recycleViewAdapter = RecycleViewAdapter().apply {
            setOnLocationLongClickListener(this@MyLocationsFragment)
        }
        binding.rvMyLocations.adapter = recycleViewAdapter
        binding.rvMyLocations.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchLocations()
        }
    }

    private fun fetchLocations() {
        val app = Gama6Application.instance
        app.fetchLocationsFromServer { fetchedLocations ->
            activity?.runOnUiThread {
                recycleViewAdapter.updateLocations(fetchedLocations)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLocationLongClicked(location: Location, position: Int) {
        showConfirmationDialog(location, position)
    }

    private fun showConfirmationDialog(location: Location, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this location?")
            .setPositiveButton("Yes") { _, _ ->
                deleteLocation(location) { success ->
                    if (success) {
                        recycleViewAdapter.removeLocationAt(position)
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private suspend fun updateLocationsIfNeeded(locations: List<Location>) {
        locations.forEachIndexed { index, location ->
            if (shouldUpdateLocation(location)) {
                val origLocation = Gama6Application.instance.locations.getOrNull(index)
                origLocation?.let {
                    updateLocation(it, index)
                }
            }
        }
    }

    private fun shouldUpdateLocation(location: Location): Boolean {
        val currentTime = System.currentTimeMillis()
        return location.simulation && currentTime - location.lastUpdated > location.updateFrequencySeconds * 1000
    }

    private fun updateLocation(location: Location, index: Int) {
        val randomNumCars = (location.minCars!!..location.maxCars!!).random()
        location.numCars = randomNumCars
        location.lastUpdated = System.currentTimeMillis()

        if (location.minCars == location.numCars) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateTime = dateFormat.format(Date())
            val message = "As of $currentDateTime, parking lot: ${location.name} is empty."
            sendMessage("Information", message)
        } else if (location.maxCars == location.numCars) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateTime = dateFormat.format(Date())
            val message = "As of $currentDateTime, parking lot: ${location.name} is full."
            sendMessage("Information", message)
        }


        ApiService.upsertLocation(location) { success ->
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    if (success) {
                        recycleViewAdapter.updateLocationAt(index, location)
                    } else {
                        Log.d("MyLocationsFragment", "Location update failed on server")
                    }
                } catch (e: Exception) {
                    Log.e("MyLocationsFragment", "Error updating location: ${e.message}")
                }
            }
        }
    }

    private fun sendMessage(messageType: String, message: String) {
        val broker = BuildConfig.BROKER
        val clientId = BuildConfig.CLIENT_ID
        val username = BuildConfig.USERNAME
        val password = BuildConfig.PASSWORD
        val topic = messageType
        val content = message

        try {
            val persistence = MemoryPersistence()
            val client = MqttClient(broker, clientId, persistence).apply {
                connect(MqttConnectOptions().apply {
                    userName = username
                    setPassword(password.toCharArray())
                })
            }

            val message = MqttMessage(content.toByteArray()).apply {
                qos = 2
            }

            client.publish(topic, message)
            Toast.makeText(context, "Message sent", Toast.LENGTH_LONG).show()
            Log.d("MyLocationsFragment", "Message sent. $message")
            client.disconnect()
            findNavController().navigateUp()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Message not sent", Toast.LENGTH_LONG).show()
        }
    }

}
