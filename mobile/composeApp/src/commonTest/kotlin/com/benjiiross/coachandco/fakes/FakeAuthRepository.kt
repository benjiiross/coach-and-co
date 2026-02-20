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
    var refreshTokenResult: Outcome<AuthResponse, AuthError> = Outcome.Failure(AuthError.Unknown("not set"))
    var loginCallCount = 0
    var registerCallCount = 0
    var refreshCallCount = 0

    override suspend fun login(authDetails: LoginRequest): Outcome<AuthResponse, AuthError> {
        loginCallCount++
        return loginResult
    }

    override suspend fun register(user: RegisterRequest): Outcome<AuthResponse, AuthError> {
        registerCallCount++
        return registerResult
    }

    override suspend fun refreshToken(token: String): Outcome<AuthResponse, AuthError> {
        refreshCallCount++
        return refreshTokenResult
    }
}
