package com.boa.weathertest.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageButton
import com.boa.weathertest.R

class SearchCardView : FrameLayout {
    var searchCardEditText: AutoCompleteTextView? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.search_card, this, true)
        val searchCardClear: ImageButton = view.findViewById(R.id.searchCardClear)
        searchCardEditText = view.findViewById(R.id.searchCardEditText)
        searchCardClear.setOnClickListener { searchCardEditText?.text = null }
    }

    fun getInput(): AutoCompleteTextView? = searchCardEditText
}