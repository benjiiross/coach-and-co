package com.benjiiross.coachandco.data.database

import com.benjiiross.coachandco.core.config.Env
import com.benjiiross.coachandco.data.database.tables.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {
  private var isInitialized = false

  fun init() {
    if (isInitialized) return

    val database = Database.connect(createHikariDataSource())

    if (Env.isDevelopment) {
      transaction(database) { SchemaUtils.create(Users) }
    }

    isInitialized = true
  }

  private fun createHikariDataSource(): HikariDataSource {
    val config =
        HikariConfig().apply {
          driverClassName = ""
          jdbcUrl = ""
          username = ""
          password = ""

          maximumPoolSize = 10
          minimumIdle = 2
          idleTimeout = 600_000
          connectionTimeout = 30_000
          maxLifetime = 1_800_000

          isAutoCommit = false
          transactionIsolation = "TRANSACTION_READ_COMMITTED"

          validate()
        }

    return HikariDataSource(config)
  }

  suspend fun <T> dbQuery(block: suspend () -> T): T =
      withContext(Dispatchers.IO) {
        suspendTransaction {
          if (Env.isDevelopment) {
            addLogger(StdOutSqlLogger)
          }
          block()
        }
      }
}
