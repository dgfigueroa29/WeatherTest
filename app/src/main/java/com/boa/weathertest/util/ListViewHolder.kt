package com.boa.weathertest.util

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boa.weathertest.R

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemTitle: TextView = view.findViewById(R.id.itemTitle)
    val itemRemove: ImageButton = view.findViewById(R.id.itemRemove)
}