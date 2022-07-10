package com.shop

import com.shop.dao.DatabaseFactory
import com.shop.plugins.configureMonitoring
import com.shop.plugins.configureRouting
import com.shop.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()
        configureRouting()
//        configureSecurity()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
