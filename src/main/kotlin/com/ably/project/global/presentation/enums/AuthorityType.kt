package com.ably.project.global.presentation.enums

import java.util.*

enum class AuthorityType(value: String) {
    USER("일반사용자"),
    SYSTEM("내부호출"),
    ADMIN("어드민");

    companion object {
        fun convertAuthorityTypeList(authList: String): List<AuthorityType> {
            val stringTokenizer = StringTokenizer(authList, ",")
            val filterList: MutableList<AuthorityType> = mutableListOf()
            while (stringTokenizer.hasMoreTokens()) {
                filterList.add(AuthorityType.valueOf(stringTokenizer.nextToken()))
            }
            return filterList
        }
    }
}

