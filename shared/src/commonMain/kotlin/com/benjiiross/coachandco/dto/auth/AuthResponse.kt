package com.benjiiross.coachandco.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserResponse,
    val refreshToken: String? = null,
)
