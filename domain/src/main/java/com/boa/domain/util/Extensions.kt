package com.boa.domain.util

import com.boa.domain.model.CityModel

fun <T> Array<T>?.toStringList(needSort: Boolean = true): List<String> {
    val enumList = mutableListOf<String>()
    this?.takeIf { it.isNotEmpty() }?.forEach {
        when (it) {
            is CityModel -> {
                enumList.add(it.name)
            }
        }
    }

    return if (needSort) {
        enumList.sorted()
    } else {
        enumList
    }
}