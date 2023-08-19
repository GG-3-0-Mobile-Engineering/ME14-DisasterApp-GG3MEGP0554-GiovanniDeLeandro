package com.example.disastergigihapp.data.remote

import javax.inject.Inject

class ApiInterfaceImpl @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getRecent(timeperiod: Int): ApiResponse = apiInterface.getRecent(timeperiod)

    suspend fun getRecentAdmin(timeperiod: Int, admin: String): ApiResponse =
        apiInterface.getRecentAdmin(timeperiod, admin)

    suspend fun getRecentDisaster(timeperiod: Int, disaster: String): ApiResponse =
        apiInterface.getRecentDisaster(timeperiod, disaster)

    suspend fun getRecentAll(timeperiod: Int, admin: String, disaster: String): ApiResponse =
        apiInterface.getRecentAll(timeperiod, admin, disaster)

}