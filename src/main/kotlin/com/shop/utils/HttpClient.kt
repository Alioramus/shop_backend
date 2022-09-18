package com.shop.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*

val appHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson()
    }
}