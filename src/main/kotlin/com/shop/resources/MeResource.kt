package com.shop.resources

import com.shop.plugins.AuthorizationException
import com.shop.plugins.UserSession
import com.shop.services.meService
import com.shop.services.ordersService
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
    @Serializable
    @Resource("/orders")
    class Orders(val parent: MeResource = MeResource()) {

    }
}

fun Routing.meRoutes() {
    get<MeResource> {
        call.respond(meService.getMe(call.sessions.get()))
    }

    get<MeResource.Orders> {
        val session: UserSession = call.sessions.get() ?: throw AuthorizationException()
        call.respond(ordersService.listOrders(session?.userId))
    }
}