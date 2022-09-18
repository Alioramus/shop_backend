package com.shop.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

fun toDTO(category: Category): CategoryDTO = CategoryDTO(category.id.value, category.name, category.description)

data class CategoryDTO(val id: Int, val name: String, val description: String)

class Category(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Category>(Categories)
    var name by Categories.name
    var description by Categories.description
}

object Categories: IntIdTable() {
    val name = varchar("name", 128)
    val description = varchar("description", 256)
}
