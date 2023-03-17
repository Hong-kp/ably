package com.ably.project.external

import com.ably.project.global.presentation.ApiFormat
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "ably", url = "\${feign.ably-api.url}")
interface AblyClient {
    @PostMapping("/api/", produces = ["application/json"])
    fun request1(
        @RequestHeader header: Map<String, String?>,
        @RequestBody body: ApiFormat<Map<String,String>>
    ): ApiFormat<Map<String,String>>
}