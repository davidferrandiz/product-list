package com.davidferrandiz.mangostore.data.remote

import android.util.Log
import com.davidferrandiz.mangostore.domain.common.MangoResult
import com.davidferrandiz.mangostore.domain.error.AppError
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "MangoNetwork"

internal suspend fun <T> safeApiCall(call: suspend () -> T): MangoResult<T> =
    try {
        MangoResult.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: SocketTimeoutException) {
        Log.w(TAG, "Timeout", e)
        MangoResult.Error(AppError.Timeout)
    } catch (e: IOException) {
        Log.w(TAG, "No connection", e)
        MangoResult.Error(AppError.NoConnection)
    } catch (e: HttpException) {
        Log.w(TAG, "HTTP ${e.code()} at ${e.response()?.raw()?.request?.url}", e)
        MangoResult.Error(AppError.Http(e.code()))
    } catch (e: SerializationException) {
        Log.w(TAG, "Parsing error", e)
        MangoResult.Error(AppError.Serialization(e.message))
    } catch (e: Exception) {
        Log.e(TAG, "Unknown error", e)
        MangoResult.Error(AppError.Unknown(e.message))
    }
