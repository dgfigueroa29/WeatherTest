package com.boa.weathertest.base

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.boa.domain.base.BaseException

/**
 * Base ViewModel for using in Model-View-ViewModel architecture. Must be specified ViewState class.
 */
abstract class BaseViewModel<V> : ViewModel() {
    val resourceViewStatus: MediatorLiveData<V> =
        MediatorLiveData<V>().apply { postValue(getInitialViewStatus()) }

    abstract fun getInitialViewStatus(): V
    abstract fun onError(exception: BaseException?)
    abstract fun onLoading(progress: Int)
}