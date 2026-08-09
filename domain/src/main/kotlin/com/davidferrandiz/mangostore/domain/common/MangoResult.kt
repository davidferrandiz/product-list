package com.davidferrandiz.mangostore.domain.common

import com.davidferrandiz.mangostore.domain.error.AppError

sealed interface MangoResult<out T> {
    data class Success<T>(val data: T) : MangoResult<T>
    data class Error(val error: AppError) : MangoResult<Nothing>
}
