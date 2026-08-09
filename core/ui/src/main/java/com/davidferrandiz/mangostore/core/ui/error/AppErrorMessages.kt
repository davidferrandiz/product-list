package com.davidferrandiz.mangostore.core.ui.error

import androidx.annotation.StringRes
import com.davidferrandiz.mangostore.core.ui.R
import com.davidferrandiz.mangostore.domain.error.AppError

@StringRes
fun AppError.toMessageRes(): Int = when (this) {
    AppError.NoConnection -> R.string.error_no_connection
    AppError.Timeout -> R.string.error_timeout
    is AppError.Http -> when (code) {
        in 500..599 -> R.string.error_server
        404 -> R.string.error_not_found
        else -> R.string.error_unknown
    }
    is AppError.Serialization -> R.string.error_serialization
    is AppError.Unknown -> R.string.error_unknown
}
