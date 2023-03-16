package com.ably.project.global.infrastructure.config

import com.ably.project.global.infrastructure.interceptor.BaseInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(BaseInterceptor())
            .addPathPatterns("/**")
    }
}



