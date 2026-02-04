package com.benjiiross.coachandco.data.tables

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock

fun <T : Table> T.softDelete(
    deletedAtColumn: Column<LocalDateTime?>,
    where: () -> Op<Boolean>,
): Int {
  return update(where = where) {
    it[deletedAtColumn] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
  }
}
