package com.benjiiross.coachandco.domain.repository

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest

interface AuthRepository {
    suspend fun login(authDetails: LoginRequest): Outcome<AuthResponse, AuthError>

    suspend fun register(user: RegisterRequest): Outcome<AuthResponse, AuthError>

    suspend fun refreshToken(token: String): Outcome<AuthResponse, AuthError>
}