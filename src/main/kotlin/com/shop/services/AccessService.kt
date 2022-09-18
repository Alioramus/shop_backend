package com.shop.services

import com.shop.plugins.UserSession

class AccessService {
    fun isAdmin(session: UserSession): Boolean {
        return session.userId == "100822381037370759843"
    }
}

val accessService: AccessService = AccessService()