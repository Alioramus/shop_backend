package com.shop.services

import com.shop.dao.DatabaseFactory.dbQuery
import com.shop.models.*
import io.ktor.server.plugins.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SizedIterable

object Errors {
    const val PRODUCT_NOT_FOUND_ERROR = "Product not found."
    const val CATEGORY_NOT_FOUND_ERROR = "Category not found."
}

class ProductsServiceImpl : ProductsService {
    override suspend fun allProducts(category: Int?): List<ProductDTO> = dbQuery {
        val products: SizedIterable<Product> = if (category == null) {
            Product.all()
        } else {
            Product.find {
                Products.category eq category
            }
        }
        products.toList().map { toDTO(it)}
    }

    override suspend fun product(id: Int): ProductDTO = dbQuery {
        val product = Product.findById(id) ?: throw NotFoundException(Errors.PRODUCT_NOT_FOUND_ERROR)
        toDTO(product)
    }

    override suspend fun addNewProduct(product: ProductDTO): ProductDTO = dbQuery {
        val categoryRecord = Category[product.category]
        val newProduct = Product.new {
            name = product.name
            description = product.description
            category = categoryRecord.id
            price = product.price
        }
        toDTO(newProduct)
    }

    override suspend fun editProduct(id: Int, name: String, description: String): ProductDTO = dbQuery {
        val product = Product.findById(id) ?: throw NotFoundException(Errors.PRODUCT_NOT_FOUND_ERROR)
        product.name = name
        product.description = description
        toDTO(product)
    }

    override suspend fun deleteProduct(id: Int) = dbQuery {
        val product = Product.findById(id) ?: throw NotFoundException(Errors.PRODUCT_NOT_FOUND_ERROR)
        product.delete()
    }
}

val productsService: ProductsService = ProductsServiceImpl().apply {
    runBlocking {
        if(allProducts().isEmpty()) {
            val food = categoriesService.addNewCategory(CategoryDTO(0, "Food", "Food and more"))
            addNewProduct(ProductDTO(0, "Sweets", "Very sweet...", food.id, 5.0))
        }
    }
}