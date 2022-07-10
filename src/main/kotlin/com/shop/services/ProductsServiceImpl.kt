package com.shop.services

import com.shop.dao.DatabaseFactory.dbQuery
import com.shop.models.Product
import com.shop.models.ProductDTO
import com.shop.models.toDTO
import io.ktor.server.plugins.*
import kotlinx.coroutines.runBlocking

class ProductsServiceImpl : ProductsService {
    override suspend fun allProducts(): List<ProductDTO> = dbQuery {
        Product.all().toList().map { toDTO(it) }
    }

    override suspend fun product(id: Int): ProductDTO = dbQuery {
        val product = Product.findById(id) ?: throw NotFoundException("Product not found.")
        toDTO(product)
    }

    override suspend fun addNewProduct(product: ProductDTO): ProductDTO = dbQuery {
        val newProduct = Product.new {
            name = product.name
            description = product.description
            price = product.price
        }
        toDTO(newProduct)
    }

    override suspend fun editProduct(id: Int, name: String, description: String): ProductDTO = dbQuery {
        val product = Product.findById(id) ?: throw NotFoundException("Product not found.")
        product.name = name
        product.description = description
        toDTO(product)
    }

    override suspend fun deleteProduct(id: Int) = dbQuery {
        val product = Product.findById(id) ?: throw NotFoundException("Product not found.")
        product.delete()
    }
}

val productsService: ProductsService = ProductsServiceImpl().apply {
    runBlocking {
        if(allProducts().isEmpty()) {
            addNewProduct(ProductDTO(0, "Sweets", "Very sweet...", 5.0))
        }
    }
}