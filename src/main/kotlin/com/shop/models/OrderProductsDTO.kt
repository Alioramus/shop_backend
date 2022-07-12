package com.shop.models

data class OrderedProductDTO(val productId: Int, val amount: Double)

data class OrderProductsDTO(val products: Collection<OrderedProductDTO>)

