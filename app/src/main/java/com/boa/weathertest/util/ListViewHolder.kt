package com.boa.weathertest.util

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemTitle: TextView = view.itemTitle
    val itemRemove: ImageButton = view.itemRemove
}