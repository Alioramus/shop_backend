package com.shop.services

import com.shop.models.ProductDTO

interface ProductsService {
    suspend fun allProducts(category: Int? = null): List<ProductDTO>
    suspend fun product(id: Int): ProductDTO
    suspend fun addNewProduct(product: ProductDTO): ProductDTO
    suspend fun editProduct(id: Int, name: String, description: String): ProductDTO
    suspend fun deleteProduct(id: Int): Unit?
}