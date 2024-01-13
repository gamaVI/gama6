package com.example.gama6mobileapp.ui.my_locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gama6mobileapp.databinding.FragmentMyLocationsBinding

class MyLocationsFragment : Fragment() {

    private var _binding: FragmentMyLocationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fabAddButton = binding.fabAddLocation
        fabAddButton.setOnClickListener { view ->
            //Open Add Location Fragment
            findNavController().navigate(com.example.gama6mobileapp.R.id.action_nav_my_locations_to_addLocationFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}