package com.example.gama6mobileapp.ui.my_locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gama6mobileapp.R
import com.example.gama6mobileapp.adapter.RecycleViewAdapter
import com.example.gama6mobileapp.databinding.FragmentMyLocationsBinding

class MyLocationsFragment : Fragment() {

    private var _binding: FragmentMyLocationsBinding? = null

    private val binding get() = _binding!!

    private lateinit var recycleViewAdapter: RecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpRecyclerView()

        binding.fabAddLocation.setOnClickListener { view ->
            //Open Add Location Fragment
            findNavController().navigate(R.id.fromMyLocationsToAddLocation)
        }

        return root
    }

    private fun setUpRecyclerView() {
        recycleViewAdapter = RecycleViewAdapter()
        binding.rvMyLocations.adapter = recycleViewAdapter
        binding.rvMyLocations.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}