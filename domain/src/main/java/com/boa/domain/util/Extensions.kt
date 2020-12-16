package com.boa.domain.util

import com.boa.domain.model.CityModel
import com.boa.domain.model.UnitType

fun <T> Collection<T>?.toStringList(needSort: Boolean = true): List<String> {
    val enumList = mutableListOf<String>()
    this?.takeIf { it.isNotEmpty() }?.forEach {
        when (it) {
            is CityModel -> {
                enumList.add(it.name)
            }

            is UnitType -> {
                enumList.add(it.text)
            }
        }
    }

    return if (needSort) {
        enumList.sorted()
    } else {
        enumList
    }
}