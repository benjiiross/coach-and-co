package com.benjiiross.coachandco.data

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date

object UsersTable : Table("users") {
  val id = integer("id").autoIncrement()
  val email = varchar("email", 255).uniqueIndex()
  val name = varchar("name", 100)
  val surname = varchar("surname", 100)
  val gender = varchar("gender", 20)
  val birthday = date("birthday")
  val phone = varchar("phone", 30)
  val isCoach = bool("is_coach").default(false)
  val isClient = bool("is_client").default(false)

  override val primaryKey = PrimaryKey(id)
}
