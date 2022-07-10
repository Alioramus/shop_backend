package com.shop.services

import com.shop.dao.DatabaseFactory.dbQuery
import com.shop.models.*
import java.util.*

class OrdersServiceImpl : OrderService {
    override suspend fun makeOrder(orderProducts: OrderProductsDTO) = dbQuery {
        var order = Order.new(UUID.randomUUID()) {}
        for (product in orderProducts.products) {
            val productRecord = Product[product.productId]
           OrderedProduct.new {
               this.order = order.id
               this.product = productRecord.id
               this.amount = product.amount
               this.price = product.amount * productRecord.price
           }
        }
    }

    override suspend fun listOrders(): List<OrderDTO> = dbQuery {
        Order.all().toList().map { toDTO(it) }
    }

    override suspend fun listProducts(orderId: UUID): OrderProductsDTO = dbQuery {
        val products = OrderedProduct.find { OrderedProducts.order eq orderId }.toList().map { toDTO(it) }
        OrderProductsDTO(products)
    }
}

val ordersService: OrderService = OrdersServiceImpl()
