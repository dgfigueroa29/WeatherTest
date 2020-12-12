package com.boa.domain.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Base UseCase that implements its behaviour. Child UseCases should implement getData with the operation to execute.
 */
abstract class BaseUseCase<T, P>(private val scope: CoroutineScope) {
    private lateinit var response: MutableLiveData<BaseResource<T>>

    fun execute(param: P): LiveData<BaseResource<T>> {
        response = MutableLiveData()
        response.postValue(
            BaseResource.loading(
                0
            )
        )

        scope.launch {
            try {
                val data = getData(param)
                response.postValue(
                    BaseResource.success(
                        data
                    )
                )
            } catch (ex: Exception) {
                response.postValue(
                    BaseResource.error(
                        BaseException(BaseError(ex.message ?: ""))
                    )
                )
            }
        }

        return response
    }

    protected abstract suspend fun getData(param: P): T

    protected fun postProgress(progress: Int) {
        response.postValue(
            BaseResource.loading(
                progress
            )
        )
    }
}