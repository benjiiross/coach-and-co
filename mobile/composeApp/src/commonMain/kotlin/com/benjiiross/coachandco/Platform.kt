package com.benjiiross.coachandco

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform