package com.example.disastergigihapp.util

import com.example.disastergigihapp.data.remote.ApiResponse

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    class Success(val data: ApiResponse) : ApiState()
    object Empty : ApiState()
}
