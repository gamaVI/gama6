package com.example.gama6mobileapp.ui.my_locations

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gama6mobileapp.R
import com.example.gama6mobileapp.adapter.RecycleViewAdapter
import com.example.gama6mobileapp.apiService.ApiService
import com.example.gama6mobileapp.apiService.ApiService.deleteLocation
import com.example.gama6mobileapp.appClass.Gama6Application
import com.example.gama6mobileapp.databinding.FragmentMyLocationsBinding
import com.example.gama6mobileapp.model.Location

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
            // Ensure UI update is run on the main thread
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

}
