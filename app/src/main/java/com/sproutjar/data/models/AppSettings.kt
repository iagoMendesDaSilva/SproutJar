package com.sproutjar.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    var theme: Theme = Theme.SPROUT_JAR,
    val language: Languages = Languages.ENGLISH,
    val biometrics: Boolean = false,
    val currency: Currency = Currency.USD,
    val notifications: Boolean = true,
)