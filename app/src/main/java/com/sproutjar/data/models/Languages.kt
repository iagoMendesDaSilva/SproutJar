package com.sproutjar.data.models

import android.util.Log
import com.sproutjar.R
import java.util.Locale


enum class Languages(val code: String) {
    PORTUGUESE("pt"),
    ENGLISH("en");

    companion object {
        private val map = values().associateBy(Languages::code)

        fun fromLanguageCode(code: String): Languages {
            return map[code] ?: throw IllegalArgumentException("Invalid code value")
        }

        fun getDeviceDefaultLanguage(): Languages {
            val locale = Locale.getDefault().language
            return when (locale) {
                "pt" -> Languages.PORTUGUESE
                else -> Languages.ENGLISH
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