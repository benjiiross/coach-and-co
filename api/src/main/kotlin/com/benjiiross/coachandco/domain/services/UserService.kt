package com.benjiiross.coachandco.domain.services

import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.domain.dto.user.UserRequest
import com.benjiiross.coachandco.domain.dto.user.UserResponse
import com.benjiiross.coachandco.domain.mappers.toModel
import com.benjiiross.coachandco.domain.mappers.toResponse
import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.repositories.IUserRepository

class UserService(private val userRepository: IUserRepository) {
  suspend fun getUserById(id: Int): UserResponse {
    return userRepository.findById(id)?.toResponse() ?: throw ResourceNotFoundException("User")
  }

  suspend fun getAllUsers(): List<UserResponse> {
    return userRepository.getAllUsers().toResponse()
  }

  suspend fun getUserByEmail(email: String): UserResponse {
    return userRepository.findByEmail(email)?.toResponse()
        ?: throw ResourceNotFoundException("User")
  }

  suspend fun getDeletedUsers(): List<UserResponse> {
    return userRepository.getDeletedUsers().toResponse()
  }

  suspend fun registerUser(request: UserRequest): UserResponse {
    val existing = userRepository.findByEmail(request.email)
    if (existing != null) throw EmailAlreadyTakenException()

    val userModel = request.toModel()

    return userRepository.createUser(userModel).toResponse()
  }

  suspend fun updateUser(id: Int, user: User): UserResponse {
    return userRepository.updateUser(userId = id, user = user)?.toResponse()
        ?: throw ResourceNotFoundException("User")
  }

  suspend fun deleteUser(id: Int) {
    val isDeleted = userRepository.deleteUser(id)
    if (!isDeleted) throw ResourceNotFoundException("User")
  }
}
