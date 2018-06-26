package com.aduer.platform.entitlement.controller.internal

import com.aduer.platform.entitlement.RoleApi
import com.aduer.platform.entitlement.enums.Role
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/role")
class RoleController: RoleApi {


    override fun getRoleById(@PathVariable("id") id: Long): Role {
        return when(id) {
            1L -> Role.Su
            2L -> Role.Merchant
            3L -> Role.Staff
            else -> Role.Member
        }
    }


}