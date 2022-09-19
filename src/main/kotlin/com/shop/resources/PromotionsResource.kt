package com.shop.resources

import com.shop.models.PromotionDTO
import com.shop.services.promotionsService
import com.shop.utils.checkAdminAccess
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/promotions")
class PromotionsResource {
    @Serializable
    @Resource("{id}")
    class Id(val parent: PromotionsResource = PromotionsResource(), val id: Int)
}

fun Routing.promotionRoutes() {
    get<PromotionsResource> {
        call.respond(promotionsService.allPromotions())
    }
    post<PromotionsResource> {
        checkAdminAccess(call)
        val promotion = call.receive<PromotionDTO>()
        val newPromotion = promotionsService.addNewPromotion(promotion)
        call.respond(HttpStatusCode.Created, newPromotion)
    }
    get<PromotionsResource.Id> {
        val promotion = promotionsService.promotion(it.id)
        call.respond(promotion)
    }
    delete<PromotionsResource.Id> {
        checkAdminAccess(call)
        promotionsService.deletePromotion(it.id)
        call.respond(HttpStatusCode.NoContent)
    }
    put<PromotionsResource.Id> {
        checkAdminAccess(call)
        val promotion = call.receive<PromotionDTO>()
        val updated = promotionsService.editPromotion(it.id, promotion.name, promotion.description)
        call.respond(updated)
    }
}