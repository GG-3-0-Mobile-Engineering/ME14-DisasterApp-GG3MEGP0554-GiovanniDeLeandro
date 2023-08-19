package com.example.disastergigihapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("reports?")
    suspend fun getRecent(
        @Query("timeperiod") timeperiod: Int?,
    ): ApiResponse

    @GET("reports?")
    suspend fun getRecentAdmin(
        @Query("timeperiod") timeperiod: Int?,
        @Query("admin") admin: String?,
    ): ApiResponse

    @GET("reports?")
    suspend fun getRecentDisaster(
        @Query("timeperiod") timeperiod: Int?,
        @Query("disaster") disaster: String?
    ): ApiResponse

    @GET("reports?")
    suspend fun getRecentAll(
        @Query("timeperiod") timeperiod: Int?,
        @Query("admin") admin: String?,
        @Query("disaster") disaster: String?
    ): ApiResponse
}