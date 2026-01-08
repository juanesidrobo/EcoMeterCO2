package com.kotlinconf.ecometer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
