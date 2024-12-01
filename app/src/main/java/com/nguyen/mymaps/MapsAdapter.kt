package com.nguyen.mymaps

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.mymaps.databinding.ItemUserMapBinding
import com.nguyen.mymaps.models.UserMap

private const val TAG = "MapsAdapter"

class MapsAdapter(val userMaps: List<UserMap>, val listener: OnClickListener) : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {
    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(val binding: ItemUserMapBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(map: UserMap) {
            binding.title.text = map.title
            binding.title.setOnClickListener {
                Log.i(TAG, "Tapped on position $adapterPosition")
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserMapBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = userMaps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userMaps[position])
    }
}
