package com.benjiiross.coachandco.domain.repository

import com.benjiiross.coachandco.core.Result
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest

interface AuthRepository {
    suspend fun login(authDetails: LoginRequest): Result<AuthResponse, AuthError>

    suspend fun register(user: RegisterRequest): Result<AuthResponse, AuthError>
}
