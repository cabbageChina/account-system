package com.aduer.platform.member.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.firewall.HttpFirewall
import org.springframework.security.web.firewall.StrictHttpFirewall

@Configuration
class WebSecurityConfig: WebSecurityConfigurerAdapter() {


    // 防止防止url带两个// spring security 5会有问题 所以必须放开这个配置
    @Bean
    fun allowUrlEncodedSlashHttpFirewall(): HttpFirewall {
        val firewall = StrictHttpFirewall()
        firewall.setAllowUrlEncodedSlash(true)
        return firewall
    }

    override fun configure(web: WebSecurity) {
        super.configure(web)
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall())
    }

    // 设置 HTTP 验证规则
    override fun configure(http: HttpSecurity) {
        // 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 / 的所有请求 都放行
                .antMatchers(
                        "/**",
                        "/login",
                        "/actuator/health",
                        "/webjars/springfox-swagger-ui/**",
                        "/swagger-ui.html",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/v2/api-docs",
                        "/swagger-resources/**").permitAll()
                // 所有 /login 的POST请求 都放行
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                // 设置session无状态！！
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    }

}