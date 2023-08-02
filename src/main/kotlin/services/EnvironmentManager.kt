package com.server.services

import io.ktor.server.application.*
class EnvironmentManager(environment:ApplicationEnvironment) {
    val secret:String = environment.config.property("jwt.secret").getString()
    val issuer:String= environment.config.property("jwt.issuer").getString()
    val audience:String= environment.config.property("jwt.audience").getString()
    val defaultRealm:String =environment.config.property("jwt.realm").getString()
}