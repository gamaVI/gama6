package com.example.gama6mobileapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gama6mobileapp.appClass.Gama6Application
import com.example.gama6mobileapp.databinding.RecycleViewItemBinding

class RecycleViewAdapter : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {
    private var locations = Gama6Application.instance.locations

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
    }

    private fun getHourMinAndSFromInt(allSeconds: Int): String {
        val hours = allSeconds / 3600
        val minutes = (allSeconds % 3600) / 60
        val seconds = allSeconds % 60
        return "$hours hours, $minutes minutes, $seconds seconds"
    }
}
