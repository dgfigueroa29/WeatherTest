@file:Suppress("unused")

package com.boa.domain.base

/**
 * Base Mapper with standard behaviour. Must be specified class to convert and result class types.
 */
abstract class BaseMapper<I, O> {
    abstract fun map(input: I): O

    fun mapAll(input: Collection<I>?): List<O> {
        val list = ArrayList<O>()

        if (input.isNullOrEmpty().not()) {
            input?.forEach {
                val mapped = map(it)

                if (!list.contains(mapped)) {
                    list.add(mapped)
                }
            }
        }

        return list
    }
}