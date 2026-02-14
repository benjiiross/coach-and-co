package com.benjiiross.coachandco.core

import kotlinx.serialization.Serializable

@Serializable
sealed class StringResource {
    @Serializable
    data class Text(val value: String) : StringResource()

    @Serializable
    data class ResourceId(val key: String, val args: Map<String, String> = emptyMap()) : StringResource()

    companion object {
        fun text(value: String) = Text(value)
        fun resource(key: String, vararg args: Pair<String, String>) = ResourceId(key, args.toMap())
    }
}