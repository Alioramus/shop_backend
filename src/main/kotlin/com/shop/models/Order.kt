package com.shop.models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

fun toDTO(order: Order) = OrderDTO(order.id.value, order.date.toString(), order.deliveryAddress, order.paymentUrl, order.paymentStatus)

data class OrderDTO(val id: UUID, val date: String, val deliveryAddress: String, val paymentUrl: String, val paymentStatus: String)

class Order(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<Order>(Orders)
    var date by Orders.date
    var orderedBy by Orders.orderedBy
    var deliveryAddress by Orders.deliveryAddress
    var paymentId by Orders.paymentId
    var paymentUrl by Orders.paymentUrl
    var paymentStatus by Orders.paymentStatus
}

object Orders: UUIDTable() {
    var date = datetime("order_date").defaultExpression(CurrentDateTime())
    var orderedBy = varchar("orderedBy", 30)
    var deliveryAddress = varchar("deliveryAddress", 256)
    var paymentId = varchar("paymentId", 256)
    var paymentUrl = varchar("paymentUrl", 1024)
    var paymentStatus = varchar("paymentStatus", 30)
}
