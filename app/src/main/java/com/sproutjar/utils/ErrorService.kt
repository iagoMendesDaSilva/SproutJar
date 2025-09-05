package com.sproutjar.utils

import com.sproutjar.R
import com.sproutjar.data.models.MessageDialog

interface ErrorService {

    companion object {
        const val HTTP_401_UNAUTHORIZED = 401
        const val HTTP_403_FORBIDDEN = 403
        const val HTTP_404_NOT_FOUND = 404
        const val HTTP_500_INTERNAL_SERVER_ERROR = 500
    }

    fun getErrorMessage(
        statusCode: Int,
        messageDefault: MessageDialog = MessageDialog(
            R.string.error_internal_server,
            R.string.checking_server
        )
    ): MessageDialog {

        return when (statusCode) {
            HTTP_401_UNAUTHORIZED -> MessageDialog(
                R.string.error_unauthorized,
                R.string.error_unauthorized
            )

            HTTP_403_FORBIDDEN -> MessageDialog(R.string.error_forbidden, R.string.error_forbidden)
            HTTP_404_NOT_FOUND -> messageDefault
            else -> MessageDialog(R.string.error_internal_server, R.string.checking_server)
        }
    }
}