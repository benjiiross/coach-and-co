package com.benjiiross.coachandco.domain.dto

import com.benjiiross.coachandco.core.enums.Gender
import com.benjiiross.coachandco.core.enums.UserType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthday: LocalDate,
    val phone: String,
    val type: UserType,
    val initials: String,
)
