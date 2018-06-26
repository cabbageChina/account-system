package com.aduer.platform.entitlement.config.swagger

import com.google.common.collect.Lists
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Profile(value = ["dev", "sit", "local"])
@Configuration
@EnableSwagger2
class SwaggerDocumentationConfiguration {

    fun apiInfo(): ApiInfo =
            ApiInfoBuilder()
                    .title("")
                    .license("")
                    .licenseUrl("")
                    .termsOfServiceUrl("")
                    .version("1.0.0")
                    .contact(Contact("", "", ""))
                    .build()

    @Bean
    fun customImplementation(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.aduer.platform.entitlement.controller"))
            .build()
            .securitySchemes(Lists.newArrayList(apiKey()))
            .securityContexts(Lists.newArrayList(securityContext()))
            .apiInfo(apiInfo())

    private fun apiKey(): ApiKey {
        return ApiKey("Authorization", "api_key", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/anyPath.*"))
                .build()
    }

    fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Lists.newArrayList(
                SecurityReference("mykey", authorizationScopes))
    }
}