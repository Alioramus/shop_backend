package com.shop.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCors() {
    install(CORS) {
        anyHost()
        allowHeader("session_id")
        exposeHeader("session_id")
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
}