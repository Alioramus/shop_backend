package com.shop.resources

import com.shop.models.CategoryDTO
import com.shop.services.categoriesService
import com.shop.utils.checkAdminAccess
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
@Resource("/categories")
class CategoriesResource {
    @Serializable
    @Resource("{id}")
    class Id(val parent: CategoriesResource = CategoriesResource(), val id: Int)
}

fun Routing.categoryRoutes() {
    get<CategoriesResource> {
        call.respond(categoriesService.allCategories())
    }
    post<CategoriesResource> {
        checkAdminAccess(call)
        val category = call.receive<CategoryDTO>()
        val newCategory = categoriesService.addNewCategory(category)
        call.respond(HttpStatusCode.Created, newCategory)
    }
    get<CategoriesResource.Id> {
        val category = categoriesService.category(it.id)
        call.respond(category)
    }
    delete<CategoriesResource.Id> {
        checkAdminAccess(call)
        categoriesService.deleteCategory(it.id)
        call.respond(HttpStatusCode.NoContent)
    }
    put<CategoriesResource.Id> {
        checkAdminAccess(call)
        val category = call.receive<CategoryDTO>()
        val updated = categoriesService.editCategory(it.id, category.name, category.description)
        call.respond(updated)
    }
}