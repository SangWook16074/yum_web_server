package com.example.yum_web_server.common.dto

data class BaseResponse<T>(
    val data : T? = null,
)
