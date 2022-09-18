package com.shop.resources

import com.shop.services.meService
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/me")
class MeResource {
}

fun Routing.meRoutes() {
    get<MeResource> {
        call.respond(meService.getMe(call.sessions.get()))
    }
}