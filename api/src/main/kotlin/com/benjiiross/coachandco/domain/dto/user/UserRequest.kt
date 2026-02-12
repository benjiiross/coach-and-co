package com.benjiiross.coachandco.domain.dto.user

import com.benjiiross.coachandco.core.enums.Gender
import com.benjiiross.coachandco.core.enums.UserType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthday: LocalDate,
    val phone: String,
    val type: UserType,
)
