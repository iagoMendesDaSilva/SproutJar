package com.sproutjar.utils

import com.sproutjar.R
import com.sproutjar.data.models.MessageInfo

interface ErrorService {

    companion object {
        const val HTTP_401_UNAUTHORIZED = 401
        const val HTTP_403_FORBIDDEN = 403
        const val HTTP_404_NOT_FOUND = 404
        const val HTTP_500_INTERNAL_SERVER_ERROR = 500
    }

    fun getErrorMessage(
        statusCode: Int,
        messageDefault: MessageInfo = MessageInfo(
            R.string.error_internal_server,
            R.string.checking_server
        )
    ): MessageInfo {

        return when (statusCode) {
            HTTP_401_UNAUTHORIZED -> MessageInfo(
                R.string.error_unauthorized,
                R.string.error_unauthorized
            )

            HTTP_403_FORBIDDEN -> MessageInfo(R.string.error_forbidden, R.string.error_forbidden)
            HTTP_404_NOT_FOUND -> messageDefault
            else -> MessageInfo(R.string.error_internal_server, R.string.checking_server)
        }
    }
}