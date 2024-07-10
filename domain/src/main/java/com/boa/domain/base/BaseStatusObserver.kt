package com.boa.domain.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.boa.domain.model.ResourceStatus
import timber.log.Timber

class BaseStatusObserver<V, T>(
    private val observer: MediatorLiveData<V>,
    private val source: LiveData<BaseResource<T>>,
    private val successCallback: (d: T?) -> Unit,
    private val errorCallback: (e: BaseException?) -> Unit,
    private val progressCallback: ((p: Int) -> Unit)
) : Observer<BaseResource<T>> {
    init {
        observer.addSource(source, this)
    }

    override fun onChanged(value: BaseResource<T>) {
        when (value.resourceStatus) {
            ResourceStatus.LOADING -> {
                progressCallback(value.progress)
            }

            ResourceStatus.SUCCESS -> {
                successCallback(value.data)
                observer.removeSource(source)
            }

            ResourceStatus.ERROR -> {
                errorCallback(value.exception)
                observer.removeSource(source)
            }

            else -> {
                Timber.e("Unknown status Base Observer")
                observer.removeSource(source)
            }
        }
    }
}