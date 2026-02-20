package com.benjiiross.coachandco.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(val refreshToken: String)
