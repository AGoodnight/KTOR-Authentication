package com.server.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.server.models.User
import com.server.services.EnvironmentManager
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import java.util.Date

fun Application.configureRouting(manager: EnvironmentManager){
    routing{
        route("/hello", HttpMethod.Get) {
            handle {
                call.respondText("Hello!")
            }
        }
        get("/bonjour"){
            call.respondText("Bonjour!")
        }
        get("/*") {
            call.respondText("Sorry, Your Lost")
        }
        post("/login") {
            val user = call.receive<User>()

            val username = user.username
            val password = user.password

            // do some authentication here

            val token = JWT.create()
                .withAudience(manager.audience)
                .withIssuer(manager.issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(manager.secret))
            call.respond(hashMapOf("token" to token))
        }
        authenticate("auth-jwt") {
            get("/user-profile"){
                call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
            }
        }
    }
}