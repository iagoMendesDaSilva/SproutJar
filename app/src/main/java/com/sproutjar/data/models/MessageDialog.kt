package com.sproutjar.data.models

import androidx.annotation.StringRes

data class MessageDialog(
    @StringRes val titleID: Int,
    @StringRes val messageID: Int,
)