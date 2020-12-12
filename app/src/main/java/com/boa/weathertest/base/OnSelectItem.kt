package com.boa.weathertest.base

/**
 * Base interface for item selection featured in lists and recycler views.
 */
interface OnSelectItem<T> {
    fun onSelectItem(item: T)
}