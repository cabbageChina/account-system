package com.aduer.platform.member.controller.su

import com.aduer.platform.member.controller.BaseController
import com.aduer.platform.member.entity.Merchant
import com.aduer.platform.member.pack.MerchantPack
import com.aduer.platform.member.service.MerchantService
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/merchant")
@Api(tags = ["su"], description = " ")
class MerchantController(
        private val merchantService: MerchantService
): BaseController(), MerchantApi {

    @GetMapping("/{id}")
    override fun get(@PathVariable("id") id: Long): Merchant {
        return merchantService.get(id)
    }

    @GetMapping
    override fun get(): List<Merchant> {
        return listOf(
                Merchant(id = 1, name = "张三", createdTime = LocalDateTime.now()),
                Merchant(id = 2, name = "李四", createdTime = LocalDateTime.now()),
                Merchant(id = 3, name = "王五", createdTime = LocalDateTime.now())
        )
    }

    @PostMapping
    override fun create(@RequestBody req: MerchantPack.MerchantCo) {
    }

    @PutMapping
    override fun update(@RequestBody req: MerchantPack.MerchantUo) {
    }
}