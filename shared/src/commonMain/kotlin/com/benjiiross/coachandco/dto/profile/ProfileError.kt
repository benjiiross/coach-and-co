package com.benjiiross.coachandco.dto.profile

import kotlinx.serialization.Serializable

@Serializable
sealed class ProfileError {
    data object Unauthorized : ProfileError()
    data object ServerError : ProfileError()
    data object NetworkError : ProfileError()
    data object NotFound : ProfileError()
    data class Unknown(val message: String) : ProfileError()
}