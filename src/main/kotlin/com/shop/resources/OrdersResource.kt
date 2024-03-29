package com.shop.resources

import com.shop.models.MakeOrderProductsDTO
import com.shop.plugins.AuthorizationException
import com.shop.plugins.UserSession
import com.shop.services.ordersService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Resource("/orders")
class OrdersResource {
    @Serializable
    @Resource("{id}")
    class Id(val parent: OrdersResource = OrdersResource(), val id: String) {
        @Serializable
        @Resource("products")
        class Products(val parent: Id)
    }
}

fun Routing.ordersRoutes() {
    get<OrdersResource> {
        call.respond(ordersService.listOrders())
    }
    post<OrdersResource> {
        val order = call.receive<MakeOrderProductsDTO>()
        val userSession: UserSession = call.sessions.get() ?: throw AuthorizationException()
        ordersService.makeOrder(order, userSession.userId)
        call.respond(HttpStatusCode.Created)
    }
    get<OrdersResource.Id.Products> {
        val products = ordersService.listProducts(UUID.fromString(it.parent.id))
        call.respond(products)
    }
}