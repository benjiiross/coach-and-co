package com.benjiiross.coachandco.dto.auth

import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthday: LocalDate,
    val phone: String,
    val type: UserType,
)