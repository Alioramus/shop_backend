package com.shop.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun toDTO(product: Product): ProductDTO = ProductDTO(product.id.value, product.name, product.description, product.price)

data class ProductDTO(val id: Int, val name: String, val description: String, val price: Double)

class Product(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Product>(Products)
    var name by Products.name
    var description by Products.description
    var price by Products.price
}

object Products: IntIdTable() {
    val name = varchar("name", 128)
    val description = varchar("description", 256)
    val price = double("price")
}