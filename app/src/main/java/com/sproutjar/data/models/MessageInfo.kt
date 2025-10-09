package com.sproutjar.data.models

import androidx.annotation.StringRes

data class MessageInfo(
    @StringRes val titleID: Int,
    @StringRes val messageID: Int,
)