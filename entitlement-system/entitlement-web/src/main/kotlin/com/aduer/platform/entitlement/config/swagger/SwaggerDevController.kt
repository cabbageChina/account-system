package com.aduer.platform.entitlement.config.swagger

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Profile("local", "dev", "sit")
@Controller
class SwaggerDevController {

    @GetMapping("/")
    fun home(): String = "redirect:swagger-ui.html"


}