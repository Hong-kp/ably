package com.ably.project.global.infrastructure.aop.annotation

import com.ably.project.global.presentation.enums.AuthorityType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class User(
    val authorityType: Array<AuthorityType>
)
