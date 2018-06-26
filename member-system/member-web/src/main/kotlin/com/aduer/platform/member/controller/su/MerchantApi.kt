package com.aduer.platform.member.controller.su

import com.aduer.platform.member.entity.Merchant
import com.aduer.platform.member.pack.MerchantPack
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

@Api(tags = ["su"], description = "")
interface MerchantApi {

    @ApiOperation(tags = ["su"], value = "商户->所有")
    fun get(@PathVariable("id") id: Long): Merchant

    @ApiOperation(tags = ["su"], value = "商户->查询")
    fun get(): List<Merchant>

    @ApiOperation(tags = ["su"], value = "商户->创建")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: MerchantPack.MerchantCo)

    @ApiOperation(tags = ["su"], value = "商户->更新")
    @ResponseStatus(HttpStatus.UPGRADE_REQUIRED)
    fun update(@RequestBody req: MerchantPack.MerchantUo)

}