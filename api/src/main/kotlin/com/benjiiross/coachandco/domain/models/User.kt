package com.benjiiross.coachandco.domain.models

import com.benjiiross.coachandco.core.enums.Gender
import com.benjiiross.coachandco.core.enums.UserType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val passwordHash: String,
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthday: LocalDate,
    val phone: String,
    val type: UserType,
)
