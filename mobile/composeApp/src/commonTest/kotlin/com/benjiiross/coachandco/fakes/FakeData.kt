package com.benjiiross.coachandco.fakes

import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.UserResponse
import kotlinx.datetime.LocalDate

val fakeUserResponse = UserResponse(
    id = 1,
    email = "john@test.com",
    firstName = "John",
    lastName = "Doe",
    gender = Gender.MALE,
    birthday = LocalDate(1990, 1, 1),
    phone = "0600000000",
    type = UserType.CLIENT,
)

val fakeAuthResponse = AuthResponse(
    token = "fake.jwt.token",
    user = fakeUserResponse,
)
