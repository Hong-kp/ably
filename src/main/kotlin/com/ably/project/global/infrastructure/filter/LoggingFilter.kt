package com.ably.project.global.infrastructure.filter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.IOException
import java.io.UnsupportedEncodingException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggingFilter : Filter {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest?, servletResponse: ServletResponse?, chain: FilterChain) {
        if (servletRequest is HttpServletRequest && servletResponse is HttpServletResponse) {
            if(servletRequest.servletPath.contains("/v1/")) {
                val requestToCache: HttpServletRequest = ContentCachingRequestWrapper(servletRequest)
                val responseToCache: HttpServletResponse = ContentCachingResponseWrapper(servletResponse)
                chain.doFilter(requestToCache, responseToCache)
                logger.info("request header: {}", getHeaders(requestToCache))
                logger.info("request body: {}", getRequestBody(requestToCache as ContentCachingRequestWrapper))
                logger.info("response body: {}", getResponseBody(responseToCache))
            }else{
                chain.doFilter(servletRequest, servletResponse)
            }
        } else {
            chain.doFilter(servletRequest, servletResponse)
        }
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, Any> {
        val headerMap: MutableMap<String, Any> = HashMap()
        val headerArray = request.headerNames
        while (headerArray.hasMoreElements()) {
            val headerName = headerArray.nextElement()
            headerMap[headerName] = request.getHeader(headerName)
        }
        return headerMap
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): String {
        val wrapper = WebUtils.getNativeRequest(
            request,
            ContentCachingRequestWrapper::class.java
        )
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                return try {
                    String(buf)
//                    String(buf, 0, buf.size, wrapper.characterEncoding)
                } catch (e: UnsupportedEncodingException) {
                    " - "
                }
            }
        }
        return " - "
    }

    private fun getResponseBody(response: HttpServletResponse): String {
        var payload: String? = null
        val wrapper = WebUtils.getNativeResponse(
            response,
            ContentCachingResponseWrapper::class.java
        )
        if (wrapper != null) {
            wrapper.characterEncoding = "UTF-8"
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
//                payload = String(buf, 0, buf.size, wrapper.characterEncoding)
                payload = String(buf)
                wrapper.copyBodyToResponse()
            }
        }
        return payload ?: " - "
    }
}
