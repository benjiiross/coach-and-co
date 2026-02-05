package com.benjiiross.coachandco.core.exceptions

class InvalidIdException(id: String?) : Exception("Id '$id' is not a valid Id.")

class ResourceNotFoundException(val resource: String) : Exception("$resource not found.")

class EmailAlreadyTakenException : Exception("Email already used.")
