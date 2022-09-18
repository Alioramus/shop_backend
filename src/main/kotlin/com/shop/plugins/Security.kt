package com.shop.plugins

import com.shop.utils.appHttpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import java.io.File

fun Application.configureSecurity(httpClient: HttpClient = appHttpClient) {
    val BACKEND_URL: String = System.getenv("BACKEND_URL") ?: "http://localhost:8080"
    val APP_URL: String = System.getenv("APP_URL") ?: "http://localhost:3000"

    install(Sessions) {
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<UserSession>("session_id", directorySessionStorage(File("build/.sessions"))) {
            cookie.path = "/"
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))

        }
    }

    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "$APP_URL/api/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
                )
            }
            client = httpClient
        }
    }

    routing {
        authenticate("auth-oauth-google") {
            get("login") {
                call.respondRedirect("/callback")
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${principal?.accessToken}")
                    }
                }.body()
                call.sessions.set(UserSession(userInfo.id, userInfo.name, principal?.accessToken.toString()))
                call.respondRedirect("$APP_URL/products")
            }
        }
    }
}

class UserSession(val userId: String, val name: String, val accessToken: String): Principal

@Serializable
class UserInfo(
    val id: String,
    val name: String,
    val picture: String,
    val locale: String
)