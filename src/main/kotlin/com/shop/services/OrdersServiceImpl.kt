package com.shop.services

import com.shop.dao.DatabaseFactory.dbQuery
import com.shop.models.*
import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import io.ktor.server.plugins.*
import java.util.*

class OrdersServiceImpl : OrderService {
    val appUrl: String = System.getenv("APP_URL") ?: "http://localhost:3000"
    override suspend fun makeOrder(orderProducts: MakeOrderProductsDTO, orderedBy: String) = dbQuery {
        val orderId = UUID.randomUUID()
        val totalPrice = calculatePrice(orderProducts.products)
        val session = createPaymentSession(orderId, totalPrice)
        var order = Order.new(orderId) {
            this.orderedBy = orderedBy
            this.deliveryAddress = orderProducts.deliveryAddress
            this.paymentId = session.id
            this.paymentUrl = session.url
            this.paymentStatus = session.status
        }
        for (product in orderProducts.products) {
            val productRecord = Product[product.productId]
           OrderedProduct.new {
               this.order = order.id
               this.product = productRecord.id
               this.amount = product.amount
               this.price = product.amount * productRecord.price
           }
        }
    }

    fun calculatePrice(orderProducts: Collection<OrderedProductDTO>): Long {
       var totalPrice = 0L;
       for (product in orderProducts) {
           val productRecord = Product[product.productId]
           totalPrice += (product.amount * productRecord.price * 100).toLong()
       }
        return totalPrice
    }

    fun createPaymentSession(orderId: UUID, price: Long): Session {

        Stripe.apiKey = System.getenv("STRIPE_KEY")

        val priceData = SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("pln")
            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder().setName("order_$orderId").build())
            .setUnitAmount(price)
            .build()
        val sessionCreate: SessionCreateParams = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("$appUrl/orders")
            .setCancelUrl("$appUrl/orders")
            .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).setPriceData(priceData).build())
            .build()
        return Session.create(sessionCreate)
    }

    override suspend fun listOrders(userId: String?): List<OrderDTO> = dbQuery {
        Stripe.apiKey = System.getenv("STRIPE_KEY")
        val orders: List<Order> = if (userId == null) {
            Order.all().toList()
        } else {
            Order.find{
                Orders.orderedBy eq userId
            }.toList()
        }
        for (order in orders) {
            if (order.paymentStatus.lowercase() == "open") {
                val orderSession = Session.retrieve(order.paymentId)
                if (orderSession.status != "open") {
                   val updateOrder = Order.findById(order.id) ?: throw NotFoundException()
                   updateOrder.paymentStatus = orderSession.status
                    order.paymentStatus = orderSession.status
                }
            }
        }
        orders.map { toDTO(it) }
    }

    override suspend fun listProducts(orderId: UUID): OrderProductsDTO = dbQuery {
        val products = OrderedProduct.find { OrderedProducts.order eq orderId }.toList().map { toDTO(it) }
        OrderProductsDTO(products)
    }
}

val ordersService: OrderService = OrdersServiceImpl()
