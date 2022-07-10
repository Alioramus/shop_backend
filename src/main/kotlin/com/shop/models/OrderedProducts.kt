package com.shop.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun toDTO(orderedProduct: OrderedProduct) = OrderedProductDTO(orderedProduct.product.value, orderedProduct.amount)

class OrderedProduct(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<OrderedProduct>(OrderedProducts)
    var order by OrderedProducts.order
    var product by OrderedProducts.product
    var amount by OrderedProducts.amount
    var price by OrderedProducts.price
}

object OrderedProducts: IntIdTable() {
    val order = reference("order", Orders)
    val product = reference("product", Products)
    val amount = double("amount")
    val price = double("price")
}
