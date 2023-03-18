package com.ably.project.global.utils

import com.ably.project.customer.presentation.dto.CustomerDTO

/**
 * 공용 스토리지
 */
object Storage {
    // 인증고객정보 (로그인 토큰 사용 프로세스에서 가능)
    var customer = ThreadLocal<CustomerDTO>()
}