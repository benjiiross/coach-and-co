package com.benjiiross.coachandco.domain.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benjiiross.coachandco.core.constants.DbConstants
import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.EmailOrPasswordIncorrect
import com.benjiiross.coachandco.core.exceptions.InvalidEmailException
import com.benjiiross.coachandco.core.exceptions.InvalidJWT
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.core.exceptions.WeakPasswordException
import com.benjiiross.coachandco.domain.dto.user.UserRequest
import com.benjiiross.coachandco.domain.dto.user.UserResponse
import com.benjiiross.coachandco.domain.mappers.toResponse
import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import io.ktor.server.auth.jwt.JWTPrincipal
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.mindrot.jbcrypt.BCrypt
import kotlin.time.Clock

class AuthService(
    private val userRepository: IUserRepository,
    private val jwtSecret: String,
    private val jwtIssuer: String,
    private val jwtAudience: String,
) {
  private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

  fun generateToken(userId: Int): String {
    val expiresAt =
        Clock.System.now().plus(1, DateTimeUnit.HOUR).toEpochMilliseconds().let {
          java.util.Date(it)
        }

    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .withClaim("userId", userId)
        .withExpiresAt(expiresAt)
        .sign(Algorithm.HMAC256(jwtSecret))
  }

  private suspend fun validateRegistration(request: UserRequest) {
    if (!request.email.matches(emailRegex)) throw InvalidEmailException()
    if (request.password.length < DbConstants.PASSWORD_MIN_LENGTH) throw WeakPasswordException()
  }

  suspend fun register(request: UserRequest): UserResponse {
    validateRegistration(request)
    if (userRepository.findByEmail(request.email) != null) throw EmailAlreadyTakenException()

    val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt(DbConstants.LOG_ROUNDS))

    val newUser =
        User(
            email = request.email,
            passwordHash = hashedPassword,
            name = request.name,
            surname = request.surname,
            gender = request.gender,
            birthday = request.birthday,
            phone = request.phone,
            type = request.type,
        )

    return userRepository.createUser(newUser).toResponse()
  }

  suspend fun login(email: String, password: String): User {
    val user = userRepository.findByEmail(email) ?: throw EmailOrPasswordIncorrect()

    if (!BCrypt.checkpw(password, user.passwordHash)) throw EmailOrPasswordIncorrect()

    return user
  }

  suspend fun me(principal: JWTPrincipal?): UserResponse {
    val userId = principal?.payload?.getClaim("userId")?.asInt() ?: throw InvalidJWT()

    val user = userRepository.findById(userId) ?: throw ResourceNotFoundException("User")

    return user.toResponse()
  }
}
