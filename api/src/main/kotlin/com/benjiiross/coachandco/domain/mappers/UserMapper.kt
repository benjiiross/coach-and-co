package com.benjiiross.coachandco.domain.mappers

import com.benjiiross.coachandco.domain.dto.UserRequest
import com.benjiiross.coachandco.domain.dto.UserResponse
import com.benjiiross.coachandco.domain.models.User

fun User.toResponse(): UserResponse {
  return UserResponse(
      id = this.id ?: 0,
      email = this.email,
      name = this.name,
      surname = this.surname,
      type = this.type,
      initials = "${this.name.first()}${this.surname.first()}".uppercase(),
      gender = this.gender,
      birthday = this.birthday,
      phone = this.phone,
  )
}

fun UserRequest.toModel(): User =
    User(
        email = this.email,
        name = this.name,
        surname = this.surname,
        gender = this.gender,
        birthday = this.birthday,
        phone = this.phone,
        type = this.type,
    )
