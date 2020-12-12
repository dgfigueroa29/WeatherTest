package com.boa.domain.base

import com.boa.domain.model.ResourceStatus

data class BaseResource<out T>(
    val resourceStatus: ResourceStatus,
    val data: T? = null,
    val exception: BaseException? = null,
    val progress: Int = 0
) {
    companion object {
        fun <T> success(data: T?): BaseResource<T> {
            return BaseResource(ResourceStatus.SUCCESS, data)
        }

        fun <T> error(exception: BaseException?): BaseResource<T> {
            return BaseResource(ResourceStatus.ERROR, exception = exception)
        }

        fun <T> loading(progress: Int): BaseResource<T> {
            return BaseResource(ResourceStatus.LOADING, progress = progress)
        }
    }
}