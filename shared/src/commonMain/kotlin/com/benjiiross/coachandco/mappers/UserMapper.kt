package com.benjiiross.coachandco.mappers

import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.dto.auth.UserResponse

fun User.toResponse() = UserResponse(
    id = requireNotNull(this.id),
    email = this.email,
    firstName = this.firstName,
    lastName = this.lastName,
    gender = this.gender,
    birthday = this.birthday,
    phone = this.phone,
    type = this.type,
)

fun RegisterRequest.toUser(hashedPw: String): User =
    User(
        email = this.email,
        passwordHash = hashedPw,
        firstName = this.firstName,
        lastName = this.lastName,
        gender = this.gender,
        birthday = this.birthday,
        phone = this.phone,
        type = this.type,
    )

fun List<User>.toResponse() = map { it.toResponse() }