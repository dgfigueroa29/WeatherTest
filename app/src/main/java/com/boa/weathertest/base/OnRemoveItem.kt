package com.boa.weathertest.base

/**
 * Base interface for item selection featured in lists and recycler views.
 */
interface OnRemoveItem<T> {
    fun onRemoveItem(item: T)
}