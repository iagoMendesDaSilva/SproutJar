package com.sproutjar.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sproutjar.R
import java.util.Locale

enum class Languages(@StringRes val title: Int, val code: String, @DrawableRes val image: Int) {
    PORTUGUESE(R.string.portugues, "pt", R.drawable.brazil_flag),
    ENGLISH(R.string.english, "en", R.drawable.usa_flag);

    companion object {
        fun getDeviceDefaultLanguage(): Languages {
            val locale = Locale.getDefault().language
            return when (locale) {
                "pt" -> Languages.PORTUGUESE
                else -> Languages.ENGLISH
            }
        }
    }
}