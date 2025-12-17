package com.narrativeprotagonist._global.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class SignInStatusType {
    PENDING,
    SUCCESS,
    EXPIRED
    ;

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(value: String): SignInStatusType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown status: $value")
    }
}