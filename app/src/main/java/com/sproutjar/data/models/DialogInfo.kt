package com.sproutjar.data.models

import com.sproutjar.R
import com.sproutjar.utils.ErrorService


data class DialogInfo (
    val messageInfo: MessageInfo =  MessageInfo(
        titleID =  R.string.error_internal_server,
        messageID =  R.string.checking_server
    ),
    val error: Int? = ErrorService.HTTP_500_INTERNAL_SERVER_ERROR,
)