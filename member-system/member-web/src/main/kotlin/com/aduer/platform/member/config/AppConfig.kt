package com.aduer.platform.member.config

import com.alibaba.druid.pool.DruidDataSource
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class AppConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val objectMapper = jacksonObjectMapper()
                .registerKotlinModule()
                .registerModule(JavaTimeModule())
                .registerModule(ParameterNamesModule())
                .registerModule(Jdk8Module())
                .registerModule(KotlinModule())

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        return objectMapper
    }

    @Bean
    fun datasource(): DataSource {

        val datasource = DruidDataSource()

        // 基本属性 url、user、password
        datasource.url = "jdbc:h2:mem:h2db"
        datasource.username = "sa"
        datasource.password = ""

        // 配置初始化大小、最小、最大
        datasource.initialSize = 5
        datasource.minIdle = 10
        datasource.maxActive = 20

        // 配置获取连接等待超时的时间
        datasource.maxWait = 60000
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.timeBetweenEvictionRunsMillis = 60000
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        datasource.minEvictableIdleTimeMillis = 300000
        // mysql可以配置为false。分库分表较多的数据库，建议配置为false。
        datasource.isPoolPreparedStatements = false

        datasource.isTestWhileIdle = false

        datasource.init()
        return datasource
    }

}