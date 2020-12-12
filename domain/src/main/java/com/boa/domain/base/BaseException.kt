package com.boa.domain.base

import java.io.IOException

data class BaseException(val error: BaseError, override val message: String = error.message) :
    IOException()