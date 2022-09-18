package com.shop.utils

import com.shop.plugins.UserSession
import com.shop.services.accessService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

suspend fun checkAdminAccess(call: ApplicationCall) {
    val userSession: UserSession? = call.sessions.get()
    if (userSession == null || !accessService.isAdmin(userSession)) {
        call.respond(HttpStatusCode.Forbidden)
    }
}