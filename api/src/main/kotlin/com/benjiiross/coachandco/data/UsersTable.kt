package com.benjiiross.coachandco.data

import com.benjiiross.coachandco.constants.DbConstants
import com.benjiiross.coachandco.domain.models.Gender
import com.benjiiross.coachandco.domain.models.UserType
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.date

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

  override val primaryKey = PrimaryKey(id)
}
