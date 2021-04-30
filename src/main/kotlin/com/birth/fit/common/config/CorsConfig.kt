package com.birth.fit.common.config

import com.birth.fit.common.filter.JwtInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.*

@Configuration
@EnableWebMvc
class CorsConfig(
    @Autowired private val jwtInterceptor: JwtInterceptor
) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/user/join")
            .excludePathPatterns("/user/login")
            .excludePathPatterns("/user/password")
            .excludePathPatterns("/email/**")
            .excludePathPatterns("/swagger-ui.html/**",
                "/swagger**", "/favicon**", "/webjars**",
                "/webjars/**","swagger-ui-**", "/v2/**",
                "/swagger-resources/**", "swagger-resources**")
    }
}