package com.ably.project.global.utils

import com.ably.project.customer.presentation.dto.CustomerDTO

/**
 * 공용 스토리지
 */
object Storage {
    // 인증고객정보
    var customerId = ThreadLocal<CustomerDTO>()
}