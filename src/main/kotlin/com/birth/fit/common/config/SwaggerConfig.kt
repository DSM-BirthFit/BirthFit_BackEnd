package com.birth.fit.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Suppress("DEPRECATION")
@Configuration
@EnableSwagger2
class SwaggerConfig(
    @Value("\${swagger.host}")
    private val swaggerHost: String
) {

    @Bean
    fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .apiInfo(this.metaInfo())
            .host(swaggerHost)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.birth.fit.domain"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun metaInfo(): ApiInfo {
        return ApiInfo(
            "BirthFit API Docs",
            "project API Docs",
            "1.0.0",
            "Terms of Service URL",
            "Contact Name",
            "License",
            "License URL"
        )
    }
}