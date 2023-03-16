package com.ably.project.global.infrastructure.aop.annotation

import com.ably.project.global.presentation.enums.ServiceType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AcceptService(
    val serviceType: Array<ServiceType>
)
