package com.example.gama6mobileapp.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gama6mobileapp.apiService.ApiService.deleteLocation
import com.example.gama6mobileapp.databinding.RecycleViewItemBinding
import com.example.gama6mobileapp.model.Location
import com.example.gama6mobileapp.ui.my_locations.MyLocationsFragment

class RecycleViewAdapter(private var locations: MutableList<Location> = mutableListOf()) :
    RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    interface OnLocationLongClickListener {
        fun onLocationLongClicked(location: Location, position: Int)
    }

    private var listener: OnLocationLongClickListener? = null

    fun setOnLocationLongClickListener(listener: OnLocationLongClickListener) {
        this.listener = listener
    }

    class ViewHolder(val binding: RecycleViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecycleViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locations[position]
        val titleString = "Free parking spots"
        holder.binding.rvTitle.text = titleString
        val rangeString = "From ${location.minCars} to ${location.maxCars} cars"
        holder.binding.rvRange.text = rangeString
        val updateFrequency = "Every ${getHourMinAndSFromInt(location.updateFrequencySeconds)}"
        holder.binding.rvUpdateFrequency.text = updateFrequency
        holder.binding.rvLocationName.text = location.name
        holder.binding.rvSimulationSwitch.isChecked = location.simulation
        val numCars = "( ${location.numCars} )"
        holder.binding.rvNumCars.text = numCars

        holder.binding.rvSimulationSwitch.apply {
            setOnCheckedChangeListener(null)
            isChecked = location.simulation

            setOnCheckedChangeListener { _, isChecked ->
                // Post the action to the handler of the itemView
                holder.itemView.post {
                    location.simulation = isChecked
                    println("Location: ${location.name} simulation state changed to $isChecked - ${location.simulation}")
                }
            }
        }

        holder.itemView.setOnLongClickListener {
            listener?.onLocationLongClicked(locations[position], position)
            true
        }

    }

    private fun getHourMinAndSFromInt(allSeconds: Int): String {
        val hours = allSeconds / 3600
        val minutes = (allSeconds % 3600) / 60
        val seconds = allSeconds % 60
        return "$hours hours, $minutes minutes, $seconds seconds"
    }

    fun updateLocations(newLocations: MutableList<Location>) {
        locations = newLocations
        notifyDataSetChanged()
    }

    fun removeLocationAt(position: Int) {
        locations.removeAt(position)
        notifyItemRemoved(position)
    }

}
