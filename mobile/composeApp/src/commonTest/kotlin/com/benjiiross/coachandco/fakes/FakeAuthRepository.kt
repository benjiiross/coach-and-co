package com.benjiiross.coachandco.fakes

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest

class FakeAuthRepository : AuthRepository {
    var loginResult: Outcome<AuthResponse, AuthError> = Outcome.Failure(AuthError.Unknown("not set"))
    var registerResult: Outcome<AuthResponse, AuthError> = Outcome.Failure(AuthError.Unknown("not set"))

    override suspend fun login(authDetails: LoginRequest): Outcome<AuthResponse, AuthError> = loginResult
    override suspend fun register(user: RegisterRequest): Outcome<AuthResponse, AuthError> = registerResult
}
