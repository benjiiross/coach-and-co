package com.benjiiross.coachandco.features.user

import com.benjiiross.coachandco.data.UsersTable
import com.benjiiross.coachandco.domain.models.User
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserRepository {
  fun createUser(user: User): Int = transaction {
    UsersTable.insert {
          it[email] = user.email
          it[name] = user.name
          it[surname] = user.surname
          it[gender] = user.gender
          it[birthday] = user.birthday
          it[phone] = user.phone
          it[type] = user.type
        }[UsersTable.id]
  }

  fun getAllUsers(): List<User> = transaction { UsersTable.selectAll().map { it.toUser() } }

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
