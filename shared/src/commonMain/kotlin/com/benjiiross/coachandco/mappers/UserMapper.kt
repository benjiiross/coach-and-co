package com.benjiiross.coachandco.mappers

import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.dto.auth.UserResponse

fun User.toResponse() = UserResponse(
    id = requireNotNull(this.id),
    email = this.email,
    name = this.name,
    surname = this.surname
)

fun User.toModel(): User =
    User(
        email = this.email,
        passwordHash = this.passwordHash,
        name = this.name,
        surname = this.surname,
        gender = this.gender,
        birthday = this.birthday,
        phone = this.phone,
        type = this.type,
    )

fun RegisterRequest.toUser(hashedPw: String): User =
    User(
        email = this.email,
        passwordHash = hashedPw,
        name = this.name,
        surname = this.surname,
        gender = this.gender,
        birthday = this.birthday,
        phone = this.phone,
        type = this.type,
    )

fun List<User>.toResponse() = map { it.toResponse() }