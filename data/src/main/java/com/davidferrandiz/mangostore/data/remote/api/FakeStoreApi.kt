package com.davidferrandiz.mangostore.data.remote.api

import com.davidferrandiz.mangostore.data.remote.response.ProductApiResponse
import com.davidferrandiz.mangostore.data.remote.response.UserApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface FakeStoreApi {

    @GET("products")
    suspend fun getProducts(): List<ProductApiResponse>

    @GET("products/{id}")
    suspend fun getProducts(@Path("id") id: Int): ProductApiResponse

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): UserApiResponse
}
