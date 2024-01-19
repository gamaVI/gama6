package com.example.gama6mobileapp.ui.take_a_picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gama6mobileapp.R
import com.example.gama6mobileapp.appClass.Gama6Application
import com.example.gama6mobileapp.databinding.FragmentTakeAPhotoBinding

class TakeAPhotoFragment : Fragment() {

    private var _binding: FragmentTakeAPhotoBinding? = null
    private var app: Gama6Application? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeAPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        app = activity?.application as? Gama6Application

        val locationNames = app?.locations?.map { it.name }?.toTypedArray() ?: arrayOf()

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, locationNames)

        binding.takeAPictureAutocompleteTextView.setAdapter(adapter)

        binding.takeAPictureGoToCameraButton.setOnClickListener {
            val locationText = binding.takeAPictureAutocompleteTextView.text.toString()
            if (validateLocation(locationNames, locationText)) {
                binding.takeAPictureAutocompleteTextView.text.clear()

                val action = TakeAPhotoFragmentDirections.fromHomeToCamera(locationText)

                findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(), "Input or select a valid location...", Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    private fun validateLocation(locationNames: Array<String>, selectedLocation: String): Boolean =
        locationNames.contains(selectedLocation)


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}