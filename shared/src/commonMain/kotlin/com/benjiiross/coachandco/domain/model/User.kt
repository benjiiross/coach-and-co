package com.benjiiross.coachandco.domain.model

import com.benjiiross.coachandco.domain.enums.Gender
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val birthday: LocalDate,
    val phone: String,
    val isCoach: Boolean = false,
)