package com.shop.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun toDTO(promotion: Promotion): PromotionDTO = PromotionDTO(promotion.id.value, promotion.name, promotion.description)

data class PromotionDTO(val id: Int, val name: String, val description: String)

class Promotion(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Promotion>(Promotions)
    var name by Promotions.name
    var description by Promotions.description
}

object Promotions: IntIdTable() {
    val name = varchar("name", 128)
    val description = varchar("description", 256)
}
