package com.example.disastergigihapp.domain.repository

import com.example.disastergigihapp.data.remote.ApiInterfaceImpl
import com.example.disastergigihapp.data.remote.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiInterfaceImpl: ApiInterfaceImpl) {

    fun getRecent(timeperiod: Int): Flow<ApiResponse> = flow {
        emit(apiInterfaceImpl.getRecent(timeperiod))
    }.flowOn(Dispatchers.IO)

    fun getRecentAdmin(timeperiod: Int, admin: String): Flow<ApiResponse> = flow {
        emit(apiInterfaceImpl.getRecentAdmin(timeperiod, admin))
    }.flowOn(Dispatchers.IO)

    fun getRecentDisaster(timeperiod: Int, disaster: String): Flow<ApiResponse> = flow {
        emit(apiInterfaceImpl.getRecentDisaster(timeperiod, disaster))
    }.flowOn(Dispatchers.IO)

    fun getRecentAll(timeperiod: Int, admin: String, disaster: String): Flow<ApiResponse> = flow {
        emit(apiInterfaceImpl.getRecentAll(timeperiod, admin, disaster))
    }.flowOn(Dispatchers.IO)
}