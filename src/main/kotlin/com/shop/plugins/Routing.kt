package com.shop.plugins

import com.shop.resources.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Resources) {
    }
    install(AutoHeadResponse)
    install(StatusPages) {
        exception<AuthenticationException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<NotFoundException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
        }

    }

    routing {
        productRoutes()
        categoryRoutes()
        ordersRoutes()
        meRoutes()
        promotionRoutes()
    }
}


class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
