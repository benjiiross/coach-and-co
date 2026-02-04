package com.benjiiross.coachandco.data.tables

import com.benjiiross.coachandco.core.constants.DbConstants
import com.benjiiross.coachandco.core.enums.Gender
import com.benjiiross.coachandco.core.enums.UserType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime

object UsersTable : Table("users") {
  val id = integer("id").autoIncrement()
  val email = varchar("email", DbConstants.EMAIL_MAX_LENGTH).uniqueIndex()
  val name = varchar("name", DbConstants.NAME_MAX_LENGTH)
  val surname = varchar("surname", DbConstants.NAME_MAX_LENGTH)
  val gender = enumerationByName("gender", DbConstants.GENDER_MAX_LENGTH, Gender::class)
  val birthday = date("birthday")
  val phone = varchar("phone", DbConstants.PHONE_MAX_LENGTH)
  val type =
      enumerationByName("user_type", DbConstants.TYPE_MAX_LENGTH, UserType::class)
          .default(UserType.CLIENT)

  val deletedAt = datetime("deleted_at").nullable()

  init {
    index(false, deletedAt)
  }

  override val primaryKey = PrimaryKey(id)
}
