package com.benjiiross.coachandco.dto.error

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val code: String,
    val message: String
)