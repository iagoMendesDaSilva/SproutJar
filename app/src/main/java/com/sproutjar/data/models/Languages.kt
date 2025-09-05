package com.sproutjar.data.models

import com.sproutjar.R


enum class Languages(val code: String) {
    PORTUGUESE("pt-br"),
    ENGLISH("en-us");

    companion object {
        private val map = values().associateBy(Languages::code)

        fun fromLanguageCode(code: String): Languages {
            return map[code] ?: throw IllegalArgumentException("Invalid code value")
        }

        fun supportedLanguages(defaultLocale: String): String {
            return when (defaultLocale) {
                "pt" -> "pt"
                else -> "en"
            }
        }

        fun localeLanguages(language: Languages): Int {
            return when (language) {
                PORTUGUESE -> R.string.portugues
                ENGLISH -> R.string.english
            }
        }

        fun flagLanguages(language: Languages): Int {
            return when (language) {
                PORTUGUESE -> R.drawable.brazil_flag
                ENGLISH -> R.drawable.usa_flag
            }
        }
    }
}