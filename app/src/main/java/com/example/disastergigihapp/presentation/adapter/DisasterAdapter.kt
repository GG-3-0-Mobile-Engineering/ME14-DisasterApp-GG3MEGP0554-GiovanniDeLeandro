package com.example.disastergigihapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import com.example.disastergigihapp.R
import com.example.disastergigihapp.data.remote.GeometriesItem
import com.example.disastergigihapp.databinding.ItemPostBinding

class DisasterAdapter(private var reports: List<GeometriesItem>) :
    RecyclerView.Adapter<DisasterAdapter.PostViewHolder>() {

    inner class PostViewHolder(private var binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(geometriesItem: GeometriesItem) {
            //show the data in recycler view (item_post.xml)
            val imageUrl = geometriesItem.properties?.imageUrl

            val placeholderImage = when (geometriesItem.properties?.disasterType) {
                "flood" -> R.drawable.flood_playstore
                "earthquake" -> R.drawable.earthquake_playstore
                "fire" -> R.drawable.fire_playstore
                "haze" -> R.drawable.haze_playstore
                "wind" -> R.drawable.wind_playstore
                "volcano" -> R.drawable.volcano_playstore
                else -> R.drawable.baseline_image_24
            }

            binding.imageBencana.setImageResource(placeholderImage)

            if (imageUrl != null) {
                // Load the image with Coil and set the placeholder image
                binding.imageBencana.load(imageUrl) {
                    placeholder(placeholderImage)
                }
                if (binding.imageBencana.willNotDraw()) {
                    binding.imageBencana.setImageResource(placeholderImage)
                }
            } else {
                binding.imageBencana.dispose()
            }

            binding.tvTitleBencana.text =
                geometriesItem.properties?.title ?: geometriesItem.properties?.disasterType
            binding.tvWaktuBencana.text = geometriesItem.properties?.createdAt
            binding.tvDeskripsiBencana.text = geometriesItem.properties?.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int = reports.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(reports[position])
    }

}