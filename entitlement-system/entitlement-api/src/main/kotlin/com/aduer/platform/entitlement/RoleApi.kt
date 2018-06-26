package com.aduer.platform.entitlement

import com.aduer.platform.entitlement.enums.Role
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@FeignClient(name = "entitlement-service", path = "/api/v1/entitlement/role")
@Api(tags = ["internal"], description = " ")
interface RoleApi {

    @ApiOperation(tags = ["internal"], value = "根据Id查询角色")
    @GetMapping("/{id}")
    fun getRoleById(@PathVariable("id") id: Long): Role

}