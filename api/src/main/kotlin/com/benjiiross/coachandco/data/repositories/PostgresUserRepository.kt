package com.benjiiross.coachandco.data.repositories

import com.benjiiross.coachandco.data.tables.UsersTable
import com.benjiiross.coachandco.data.tables.softDelete
import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class PostgresUserRepository : IUserRepository {
  override suspend fun getAllUsers(): List<User> = transaction {
    UsersTable.selectAll().map { it.toUser() }
  }

  override suspend fun findById(userId: Int): User? = transaction {
    UsersTable.selectAll().where { UsersTable.id eq userId }.singleOrNull()?.toUser()
  }

  override suspend fun findByEmail(email: String): User? = transaction {
    UsersTable.selectAll().where { UsersTable.email eq email }.singleOrNull()?.toUser()
  }

  override suspend fun createUser(user: User): User = transaction {
    val id =
        UsersTable.insert {
              it[email] = user.email
              it[name] = user.name
              it[surname] = user.surname
              it[gender] = user.gender
              it[birthday] = user.birthday
              it[phone] = user.phone
              it[type] = user.type
            }[UsersTable.id]

    user.copy(id = id)
  }

  override suspend fun updateUser(userId: Int, user: User): User? = transaction {
    val updated =
        UsersTable.update(where = { UsersTable.id eq userId }) {
          it[name] = user.name
          it[surname] = user.surname
          it[gender] = user.gender
          it[birthday] = user.birthday
          it[phone] = user.phone
        }

    if (updated > 0) user.copy(id = userId) else null
  }

  override suspend fun deleteUser(userId: Int): Boolean = transaction {
    UsersTable.softDelete(
        deletedAtColumn = UsersTable.deletedAt,
        where = { UsersTable.id eq userId },
    ) > 0
  }

  private fun ResultRow.toUser() =
      User(
          id = this[UsersTable.id],
          email = this[UsersTable.email],
          name = this[UsersTable.name],
          surname = this[UsersTable.surname],
          gender = this[UsersTable.gender],
          birthday = this[UsersTable.birthday],
          phone = this[UsersTable.phone],
          type = this[UsersTable.type],
      )
}
