package com.shop.services

import com.shop.dao.DatabaseFactory
import com.shop.models.Category
import com.shop.models.CategoryDTO
import com.shop.models.toDTO
import io.ktor.server.plugins.*

class CategoriesService {
    suspend fun allCategories(): List<CategoryDTO> = DatabaseFactory.dbQuery {
        Category.all().toList().map { toDTO(it) }
    }

    suspend fun category(id: Int): CategoryDTO = DatabaseFactory.dbQuery {
        val category = Category.findById(id) ?: throw NotFoundException(Errors.CATEGORY_NOT_FOUND_ERROR)
        toDTO(category)
    }

    suspend fun addNewCategory(category: CategoryDTO): CategoryDTO = DatabaseFactory.dbQuery {
        val newCategory = Category.new {
            name = category.name
            description = category.description
        }
        toDTO(newCategory)
    }

    suspend fun editCategory(id: Int, name: String, description: String): CategoryDTO = DatabaseFactory.dbQuery {
        val category = Category.findById(id) ?: throw NotFoundException(Errors.CATEGORY_NOT_FOUND_ERROR)
        category.name = name
        category.description = description
        toDTO(category)
    }

    suspend fun deleteCategory(id: Int) = DatabaseFactory.dbQuery {
        val category = Category.findById(id) ?: throw NotFoundException(Errors.CATEGORY_NOT_FOUND_ERROR)
        category.delete()
    }
}

val categoriesService: CategoriesService = CategoriesService()
