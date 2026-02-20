package com.benjiiross.coachandco.dto.profile

import com.benjiiross.coachandco.domain.enums.Gender
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val gender: Gender? = null,
    val birthday: String? = null,
    val phone: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null,
)
