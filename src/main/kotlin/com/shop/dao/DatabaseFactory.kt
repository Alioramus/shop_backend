package com.shop.dao

import com.shop.models.OrderedProducts
import com.shop.models.Orders
import com.shop.models.Products
import com.shop.models.Promotions
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val jdbcUrl = "jdbc:sqlite:db.sqlite"
        val database = Database.connect(jdbcUrl)
        transaction(database) {
            SchemaUtils.create(Products, Orders, OrderedProducts, Promotions)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}