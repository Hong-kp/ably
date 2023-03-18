package com.ably.project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableFeignClients
@EnableAspectJAutoProxy
@EnableJpaAuditing
@SpringBootApplication
class AblyApplication

fun main(args: Array<String>) {
    runApplication<AblyApplication>(*args)
    println("this is ably server")
}

