package com.benjiiross.coachandco.data.database

import com.benjiiross.coachandco.data.database.tables.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {
    private var isInitialized = false

    fun init(config: ApplicationConfig) {
        if (isInitialized) return

        val database = Database.connect(createHikariDataSource(config = config))

        if (config.propertyOrNull("isDevelopment")?.getString().toBoolean()) {
            transaction(database) { SchemaUtils.create(Users) }
        }

        isInitialized = true
    }

    private fun createHikariDataSource(config: ApplicationConfig): HikariDataSource {
        val config =
            HikariConfig().apply {
                driverClassName = config.property("database.driver").getString()
                jdbcUrl = config.property("database.url").getString()
                username = config.property("database.username").getString()
                password = config.property("database.password").getString()

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
                addLogger(StdOutSqlLogger)

                block()
            }
        }
}
