package io.github.suwasto.showcase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform