package com.benjiiross.coachandco.domain.repositories

import com.benjiiross.coachandco.domain.models.User

interface IUserRepository {
  suspend fun getAllUsers(): List<User>

  suspend fun findById(userId: Int): User?

  suspend fun findByEmail(email: String): User?

  suspend fun createUser(user: User): User

  suspend fun updateUser(userId: Int, user: User): User?

  suspend fun deleteUser(userId: Int): Boolean
}
