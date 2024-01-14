package com.example.gama6mobileapp.ui.my_locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gama6mobileapp.R
import com.example.gama6mobileapp.adapter.RecycleViewAdapter
import com.example.gama6mobileapp.appClass.Gama6Application
import com.example.gama6mobileapp.databinding.FragmentMyLocationsBinding

class MyLocationsFragment : Fragment() {

    private var _binding: FragmentMyLocationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recycleViewAdapter: RecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpRecyclerView()
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
        recycleViewAdapter = RecycleViewAdapter()
        binding.rvMyLocations.adapter = recycleViewAdapter
        binding.rvMyLocations.layoutManager = LinearLayoutManager(context)
    }

    private fun fetchLocations() {
        val app = Gama6Application.instance
        app.fetchLocationsFromServer { fetchedLocations ->
            // Ensure UI update is run on the main thread
            activity?.runOnUiThread {
                recycleViewAdapter.updateLocations(fetchedLocations)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
