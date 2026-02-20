package com.benjiiross.coachandco.dto.auth

import com.benjiiross.coachandco.domain.enums.Gender
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val birthday: LocalDate,
    val phone: String,
    val isCoach: Boolean,
)