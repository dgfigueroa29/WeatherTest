package com.boa.weathertest.util

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemRoot: ConstraintLayout = view.itemRoot
    val itemTitle: TextView = view.itemTitle
    val itemSubTitle: TextView = view.itemSubTitle
    val itemValue: TextView = view.itemValue
}