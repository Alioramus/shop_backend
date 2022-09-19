package com.shop.models

data class OrderedProductDTO(val productId: Int, val amount: Double)

data class MakeOrderProductsDTO(val products: Collection<OrderedProductDTO>, val deliveryAddress: String)

data class OrderProductsDTO(val products: Collection<OrderedProductDTO>)

