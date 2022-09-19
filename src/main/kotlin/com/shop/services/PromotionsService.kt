package com.shop.services

import com.shop.dao.DatabaseFactory
import com.shop.models.Promotion
import com.shop.models.PromotionDTO
import com.shop.models.toDTO
import io.ktor.server.plugins.*

class PromotionsService {
    suspend fun allPromotions(): List<PromotionDTO> = DatabaseFactory.dbQuery {
        Promotion.all().toList().map { toDTO(it) }
    }

    suspend fun promotion(id: Int): PromotionDTO = DatabaseFactory.dbQuery {
        val promotion = Promotion.findById(id) ?: throw NotFoundException(Errors.CATEGORY_NOT_FOUND_ERROR)
        toDTO(promotion)
    }

    suspend fun addNewPromotion(promotion: PromotionDTO): PromotionDTO = DatabaseFactory.dbQuery {
        val newPromotion = Promotion.new {
            name = promotion.name
            description = promotion.description
        }
        toDTO(newPromotion)
    }

    suspend fun editPromotion(id: Int, name: String, description: String): PromotionDTO = DatabaseFactory.dbQuery {
        val promotion = Promotion.findById(id) ?: throw NotFoundException(Errors.CATEGORY_NOT_FOUND_ERROR)
        promotion.name = name
        promotion.description = description
        toDTO(promotion)
    }

    suspend fun deletePromotion(id: Int) = DatabaseFactory.dbQuery {
        val promotion = Promotion.findById(id) ?: throw NotFoundException(Errors.CATEGORY_NOT_FOUND_ERROR)
        promotion.delete()
    }
}

val promotionsService: PromotionsService = PromotionsService()
