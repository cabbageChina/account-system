package com.aduer.platform.member.service

import com.aduer.platform.member.entity.Merchant

interface MerchantService {

    fun get(id: Long): Merchant

}