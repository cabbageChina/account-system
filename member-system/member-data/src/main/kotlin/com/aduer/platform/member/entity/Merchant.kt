package com.aduer.platform.member.entity

import java.time.LocalDateTime

data class Merchant(
        val id: Long,

        val name: String,

        val createdTime: LocalDateTime

)