package com.aduer.platform.member.controller

import com.aduer.platform.entitlement.RoleApi
import com.aduer.platform.entitlement.enums.Role
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["dev"], description = " ")
@RequestMapping("/dev")
class DemoController(
        private val roleApi: RoleApi
) {

    @GetMapping("/sayHello")
    fun sayHello(): String {
        return "hello world"
    }

    @GetMapping("/role/{id}")
    @ApiOperation(tags = ["dev"], value = "根据Id查询角色(远程调用)")
    fun getRoleById(@PathVariable("id") id: Long): Role {
        return roleApi.getRoleById(id)
    }




}