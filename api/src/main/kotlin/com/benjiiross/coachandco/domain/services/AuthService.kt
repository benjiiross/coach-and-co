package com.benjiiross.coachandco.domain.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.EmailOrPasswordIncorrect
import com.benjiiross.coachandco.core.exceptions.InvalidJWT
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.domain.repository.UserRepository
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.mappers.toResponse
import com.benjiiross.coachandco.mappers.toUser
import io.ktor.server.auth.jwt.JWTPrincipal
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.mindrot.jbcrypt.BCrypt
import java.util.Date
import kotlin.time.Clock

class AuthService(
    private val userRepository: UserRepository,
    private val jwtSecret: String,
    private val jwtDomain: String,
    private val jwtAudience: String,
) {
    fun generateToken(userId: Int): String {
        val expiresAt =
            Clock.System.now().plus(1, DateTimeUnit.HOUR).toEpochMilliseconds().let {
                Date(it)
            }

        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("userId", userId)
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    suspend fun login(email: String, password: String): User {
        val user = userRepository.findByEmail(email) ?: throw EmailOrPasswordIncorrect()

        if (!BCrypt.checkpw(password, user.passwordHash)) throw EmailOrPasswordIncorrect()

        return user
    }

    suspend fun register(request: RegisterRequest): User {
        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null) throw EmailAlreadyTakenException()

        val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())

        val user = userRepository.createUser(user = request.toUser(passwordHash))

        return user
    }

    suspend fun me(principal: JWTPrincipal?): UserResponse {
        val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw InvalidJWT()

        val user = userRepository.findById(userId) ?: throw ResourceNotFoundException("User")

        return user.toResponse()
    }
}
