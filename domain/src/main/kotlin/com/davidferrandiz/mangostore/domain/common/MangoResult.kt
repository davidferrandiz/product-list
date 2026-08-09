package com.davidferrandiz.mangostore.domain.common

import com.davidferrandiz.mangostore.domain.error.AppError

sealed interface MangoResult<out T> {
    data class Success<T>(val data: T) : MangoResult<T>
    data class Error(val error: AppError) : MangoResult<Nothing>
}

inline fun <T, R> MangoResult<T>.map(transform: (T) -> R): MangoResult<R> =
    when (this) {
        is MangoResult.Success -> MangoResult.Success(transform(data))
        is MangoResult.Error -> this
    }
