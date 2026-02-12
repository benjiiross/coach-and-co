package com.benjiiross.coachandco.core.exceptions

import io.ktor.http.HttpStatusCode

sealed class AppContextException(val statusCode: HttpStatusCode, override val message: String) :
    Exception(message)

class InvalidIdException(id: String?) :
    AppContextException(HttpStatusCode.BadRequest, "Id '$id' is not a valid Id.")

class ResourceNotFoundException(resource: String) :
    AppContextException(HttpStatusCode.NotFound, "$resource not found.")

class EmailAlreadyTakenException :
    AppContextException(HttpStatusCode.Conflict, "Email already used.")

class EmailOrPasswordIncorrect :
    AppContextException(HttpStatusCode.Unauthorized, "Incorrect Email or Password.")

class InvalidEmailException :
    AppContextException(HttpStatusCode.BadRequest, "Invalid Email format.")

class WeakPasswordException : AppContextException(HttpStatusCode.BadRequest, "Password too weak.")

class InvalidJWT : AppContextException(HttpStatusCode.Unauthorized, "Unauthorized.")

class ForbiddenException : AppContextException(HttpStatusCode.Forbidden, "Unauthorized.")
