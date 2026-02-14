package com.benjiiross.coachandco.domain.error

import com.benjiiross.coachandco.core.StringResource
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthError {
    abstract val messageKey: String
    abstract fun toStringResource(): StringResource

    @Serializable
    data object InvalidCredentials : AuthError() {
        override val messageKey = "error_invalid_credentials"
        override fun toStringResource() = StringResource.resource("error_invalid_credentials")
    }

    @Serializable
    data object NetworkError : AuthError() {
        override val messageKey = "error_network"
        override fun toStringResource() = StringResource.resource("error_network")
    }

    @Serializable
    data object ServerError : AuthError() {
        override val messageKey = "error_server"
        override fun toStringResource() = StringResource.resource("error_server")
    }

    @Serializable
    data object Unauthorized : AuthError() {
        override val messageKey = "error_unauthorized"
        override fun toStringResource() = StringResource.resource("error_unauthorized")
    }

    @Serializable
    data class Unknown(val message: String) : AuthError() {
        override val messageKey = "error_unknown"
        override fun toStringResource() = StringResource.resource("error_unknown", "message" to message)
    }
}