package com.benjiiross.coachandco.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
)