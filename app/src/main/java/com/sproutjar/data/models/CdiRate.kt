package com.sproutjar.data.models
import com.google.gson.annotations.SerializedName

data class CdiRate (
    @SerializedName("data") val date: String,
    @SerializedName("valor") val value: String,
)