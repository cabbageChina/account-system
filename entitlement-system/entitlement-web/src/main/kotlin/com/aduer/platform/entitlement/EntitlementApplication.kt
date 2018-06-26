package com.aduer.platform.entitlement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class EntitlementApplication

fun main(args: Array<String>) {
    runApplication<EntitlementApplication>()
}