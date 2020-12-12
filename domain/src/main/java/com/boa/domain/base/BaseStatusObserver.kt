package com.boa.domain.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.boa.domain.model.ResourceStatus

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

    override fun onChanged(t: BaseResource<T>?) {
        when (t?.resourceStatus) {
            ResourceStatus.LOADING -> {
                progressCallback(t.progress)
            }

            ResourceStatus.SUCCESS -> {
                successCallback(t.data)
                observer.removeSource(source)
            }

            ResourceStatus.ERROR -> {
                errorCallback(t.exception)
                observer.removeSource(source)
            }
        }
    }
}