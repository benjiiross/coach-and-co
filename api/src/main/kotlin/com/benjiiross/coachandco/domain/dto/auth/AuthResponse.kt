package com.benjiiross.coachandco.domain.dto.auth

import com.benjiiross.coachandco.domain.dto.user.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserResponse,
)
