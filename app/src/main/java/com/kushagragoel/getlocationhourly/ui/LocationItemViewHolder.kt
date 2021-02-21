package com.kushagragoel.getlocationhourly.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kushagragoel.getlocationhourly.R
import com.kushagragoel.getlocationhourly.database.LocationEntity

class LocationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val latValue = itemView.findViewById<TextView>(R.id.latitudeValueTextView)
    private val longValue = itemView.findViewById<TextView>(R.id.longitudeValuetextView)
    private val timeValue = itemView.findViewById<TextView>(R.id.timeValueTextView)

    fun onBind(location: LocationEntity) {
        latValue.text = location.latitude.toString()
        longValue.text = location.longitude.toString()
        timeValue.text = location.time.toString()
    }
}