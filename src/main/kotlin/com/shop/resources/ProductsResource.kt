package com.shop.resources

import com.shop.models.ProductDTO
import com.shop.services.productsService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/products")
class ProductsResource {
    @Serializable
    @Resource("{id}")
    class Id(val parent: ProductsResource = ProductsResource(), val id: Int)
}

fun Routing.productRoutes() {
    get<ProductsResource> {
        call.respond(productsService.allProducts())
    }
    post<ProductsResource> {
        val product = call.receive<ProductDTO>()
        val newProduct = productsService.addNewProduct(product)
        call.respond(HttpStatusCode.Created, newProduct)
    }
    get<ProductsResource.Id> {
        val product = productsService.product(it.id)
        call.respond(product)
    }
    delete<ProductsResource.Id> {
        productsService.deleteProduct(it.id)
        call.respond(HttpStatusCode.NoContent)
    }
    put<ProductsResource.Id> {
        val product = call.receive<ProductDTO>()
        val updated = productsService.editProduct(it.id, product.name, product.description)
        call.respond(updated)
    }
}