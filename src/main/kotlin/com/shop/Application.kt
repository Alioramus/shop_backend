package com.shop

import com.shop.dao.DatabaseFactory
import com.shop.plugins.configureMonitoring
import com.shop.plugins.configureRouting
import com.shop.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import com.shop.plugins.*

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()
        configureRouting()
        configureCors()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
