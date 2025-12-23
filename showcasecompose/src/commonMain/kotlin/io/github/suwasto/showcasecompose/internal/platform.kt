package io.github.suwasto.showcasecompose.internal

internal enum class Platform {
    ANDROID,
    IOS
}

internal expect fun getCurrentPlatform(): Platform
