package com.boa.data.datasource.remote

import com.boa.data.datasource.remote.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface AppApi {
    @GET
    suspend fun getOneCall(@Url url: String): ApiResponse
}