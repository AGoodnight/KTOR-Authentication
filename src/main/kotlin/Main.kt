package com.server

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.server.auth.configureAuthorization
import com.server.plugins.configureRouting
import com.server.plugins.configureSerialization
import com.server.services.EnvironmentManager
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.configureBasicAuthentication(){
    install(Authentication) {
        jwt ("auth-bearer") {
            // Configure basic authentication
            realm = "Access to the '/' path"

        }
    }
}

fun Application.configureCORS() {
    install(CORS){
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader("AccessToken")
        allowHeader("RefreshToken")
        allowCredentials = true
        anyHost()
    }
}

fun Application.module() {
    val envManager = EnvironmentManager(environment)
    configureCORS()
    configureSerialization()
    configureAuthorization(envManager)
    configureRouting(envManager)
}