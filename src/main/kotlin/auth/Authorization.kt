package com.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.server.services.EnvironmentManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthorization(envManager:EnvironmentManager){
    install(Authentication) {
        jwt("auth-jwt") {
            realm = envManager.defaultRealm
            verifier(
                JWT
                .require(Algorithm.HMAC256(envManager.secret))
                .withAudience(envManager.audience)
                .withIssuer(envManager.issuer)
                .build())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}