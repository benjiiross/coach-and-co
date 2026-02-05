package com.benjiiross.coachandco.database.repositories

import com.benjiiross.coachandco.database.tables.Users
import com.benjiiross.coachandco.database.tables.softDelete
import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class UserRepositoryImpl : IUserRepository {
  override suspend fun getAllUsers(): List<User> = transaction {
    Users.selectAll().where { Users.deletedAt.isNull() }.map { it.toUser() }
  }

  override suspend fun findById(userId: Int): User? = transaction {
    Users.selectAll()
        .where { (Users.id eq userId) and (Users.deletedAt.isNull()) }
        .singleOrNull()
        ?.toUser()
  }

  override suspend fun findByEmail(email: String): User? = transaction {
    Users.selectAll()
        .where { (Users.email eq email) and (Users.deletedAt.isNull()) }
        .singleOrNull()
        ?.toUser()
  }

  override suspend fun createUser(user: User): User = transaction {
    val id =
        Users.insert {
              it[email] = user.email
              it[name] = user.name
              it[surname] = user.surname
              it[gender] = user.gender
              it[birthday] = user.birthday
              it[phone] = user.phone
              it[type] = user.type
            }[Users.id]

    user.copy(id = id)
  }

  override suspend fun updateUser(userId: Int, user: User): User? = transaction {
    val updated =
        Users.update(where = { Users.id eq userId }) {
          it[name] = user.name
          it[surname] = user.surname
          it[gender] = user.gender
          it[birthday] = user.birthday
          it[phone] = user.phone
        }

    if (updated > 0) user.copy(id = userId) else null
  }

  override suspend fun deleteUser(userId: Int): Boolean = transaction {
    Users.softDelete(
        deletedAtColumn = Users.deletedAt,
        where = { Users.id eq userId },
    ) > 0
  }

  private fun ResultRow.toUser() =
      User(
          id = this[Users.id],
          email = this[Users.email],
          name = this[Users.name],
          surname = this[Users.surname],
          gender = this[Users.gender],
          birthday = this[Users.birthday],
          phone = this[Users.phone],
          type = this[Users.type],
      )
}
