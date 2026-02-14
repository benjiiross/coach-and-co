package com.benjiiross.coachandco.domain.repository

import com.benjiiross.coachandco.domain.model.User

interface UserRepository {
  suspend fun getAllUsers(): List<User>

  suspend fun getDeletedUsers(): List<User>

  suspend fun findById(userId: Int): User?

  suspend fun findByEmail(email: String): User?

  suspend fun createUser(user: User): User

  suspend fun updateUser(userId: Int, user: User): User?

  suspend fun deleteUser(userId: Int): Boolean
}