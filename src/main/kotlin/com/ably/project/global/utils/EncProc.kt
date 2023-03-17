package com.ably.project.global.utils

import java.nio.charset.Charset
import java.security.MessageDigest

object EncProc {
    fun getHash(plainText:String): String {
        var sha256 = ""

        runCatching {
            val mdSHA256: MessageDigest = MessageDigest.getInstance("SHA-256")

            // 데이터(패스워드 평문)를 한다. 즉 '암호화'와 유사한 개념
            mdSHA256.update(plainText.toByteArray(Charset.forName("UTF-8")))

            // 바이트 배열로 해쉬를 반환한다.
            val sha256Hash: ByteArray = mdSHA256.digest()

            // StringBuffer 객체는 계속해서 append를 해도 객체는 오직 하나만 생성된다. => 메모리 낭비 개선
            val hexSHA256hash = StringBuffer()

            // 256비트로 생성 => 32Byte => 1Byte(8bit) => 16진수 2자리로 변환 => 16진수 한 자리는 4bit
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