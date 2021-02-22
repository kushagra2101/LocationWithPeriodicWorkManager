package com.kushagragoel.getlocationhourly.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kushagragoel.getlocationhourly.R
import com.kushagragoel.getlocationhourly.database.LocationEntity

class LocationDataRecyclerViewAdapter: PagedListAdapter<LocationEntity, LocationItemViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return LocationItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationItemViewHolder, position: Int) {
        val locationItem = getItem(position)
        holder.onBind(locationItem!!)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationEntity>() {
            override fun areItemsTheSame(
                oldItem: LocationEntity,
                newItem: LocationEntity
            ): Boolean {
                return oldItem.locationId == newItem.locationId
            }

            override fun areContentsTheSame(
                oldItem: LocationEntity,
                newItem: LocationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}