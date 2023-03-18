package com.ably.project.global.utils

import java.nio.charset.Charset
import java.security.MessageDigest

object EncProc {
    fun getHash(plainText:String): String {
        var sha256 = ""

        runCatching {
            val mdSHA256: MessageDigest = MessageDigest.getInstance("SHA-256")

            mdSHA256.update(plainText.toByteArray(Charset.forName("UTF-8")))
            val sha256Hash: ByteArray = mdSHA256.digest()
            val hexSHA256hash = StringBuffer()
            for (b in sha256Hash) {
                val hexString = String.format("%02x", b)
                hexSHA256hash.append(hexString)
            }

            sha256 = hexSHA256hash.toString()
        }.onFailure {
            println("암호화 해시알고리즘 처리 실패")
        }

        return sha256
    }
}