package com.benjiiross.coachandco.domain.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>

    suspend fun register(email: String, password: String): Result<User>
}
