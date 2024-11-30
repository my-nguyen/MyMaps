package com.nguyen.mymaps

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.mymaps.models.UserMap

private const val TAG = "MapsAdapter"

class MapsAdapter(val userMaps: List<UserMap>, val listener: OnClickListener) : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {
    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(map: UserMap) {
            view.findViewById<TextView>(android.R.id.text1).text = map.title

            view.setOnClickListener {
                Log.i(TAG, "Tapped on position $adapterPosition")
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = userMaps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userMaps[position])
    }
}
