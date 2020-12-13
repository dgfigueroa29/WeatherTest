package com.boa.weathertest.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import com.boa.weathertest.R
import kotlinx.android.synthetic.main.search_card.view.*

class SearchCardView : FrameLayout {
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
        LayoutInflater.from(context).inflate(R.layout.search_card, this, true)
        searchCardClear.setOnClickListener { searchCardEditText.text = null }
    }

    fun getInput(): AutoCompleteTextView = searchCardEditText
}