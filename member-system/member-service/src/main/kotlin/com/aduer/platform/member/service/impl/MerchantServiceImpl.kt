package com.aduer.platform.member.service.impl

import com.aduer.platform.member.entity.Merchant
import com.aduer.platform.member.service.MerchantService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MerchantServiceImpl: MerchantService {

    override fun get(id: Long): Merchant {
        return Merchant(id = 1L, name = "张三", createdTime = LocalDateTime.now())
    }
}