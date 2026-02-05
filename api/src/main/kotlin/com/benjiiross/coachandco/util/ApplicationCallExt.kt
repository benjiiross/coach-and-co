package com.benjiiross.coachandco.util

import com.benjiiross.coachandco.core.exceptions.InvalidIdException
import io.ktor.server.application.ApplicationCall

fun ApplicationCall.receiveId(paramName: String = "id"): Int {
  val rawId = parameters[paramName]
  return rawId?.toIntOrNull() ?: throw InvalidIdException(rawId)
}
