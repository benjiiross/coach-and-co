package com.benjiiross.coachandco.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val name: String,
    val surname: String,
    val gender: String,
    val birthday: LocalDate,
    val phone: String,
    val isCoach: Boolean,
    val isClient: Boolean,
)
