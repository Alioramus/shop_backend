package com.shop.services

import com.shop.models.MakeOrderProductsDTO
import com.shop.models.OrderDTO
import com.shop.models.OrderProductsDTO
import java.util.*

interface OrderService {
    suspend fun listOrders(userId: String? = null): List<OrderDTO>
    suspend fun listProducts(orderId: UUID): OrderProductsDTO
    suspend fun makeOrder(products: MakeOrderProductsDTO, orderedBy: String)
}