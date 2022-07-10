package com.shop.services

import com.shop.models.OrderDTO
import com.shop.models.OrderProductsDTO
import java.util.*

interface OrderService {
    suspend fun listOrders(): List<OrderDTO>
    suspend fun listProducts(orderId: UUID): OrderProductsDTO
    suspend fun makeOrder(products: OrderProductsDTO)
}