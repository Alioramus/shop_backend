package com.shop.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

fun toDTO(order: Order) = OrderDTO(order.id.value, order.date.toString())

data class OrderDTO(val id: UUID, val date: String)

class Order(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<Order>(Orders)
    var date by Orders.date
}

object Orders: UUIDTable() {
    var date = datetime("order_date").defaultExpression(CurrentDateTime())
}
