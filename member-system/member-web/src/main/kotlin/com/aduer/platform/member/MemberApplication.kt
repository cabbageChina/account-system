package com.aduer.platform.member

import com.aduer.platform.entitlement.RoleApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(clients = [RoleApi::class])
class MemberApplication

fun main(args: Array<String>) {
    runApplication<MemberApplication>()
}