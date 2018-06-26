package com.aduer.platform.member.pack

import io.swagger.annotations.ApiModelProperty

sealed class MerchantPack {

    data class MerchantCo(
            @ApiModelProperty("商户名称")
            val name: String
    )

    data class MerchantUo(
            @ApiModelProperty("商户名称")
            val name: String
    )

}