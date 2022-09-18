package com.shop.services

import com.shop.plugins.UserSession

data class MeDTO(val name: String, val isLoggedIn: Boolean, val isAdmin: Boolean)
val ANONYMOUS = MeDTO("ANONYMOUS", isLoggedIn = false, isAdmin = false)

class MeService {
   fun getMe(session: UserSession?): MeDTO {
       println(session)
       if (session == null) {
           return ANONYMOUS
       }
       return MeDTO(session.name, true, accessService.isAdmin(session))
   }
}

val meService = MeService()