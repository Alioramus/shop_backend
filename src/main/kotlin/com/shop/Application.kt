package com.shop

import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import com.shop.plugins.*

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0") {
        configureRouting()
//        configureSecurity()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
