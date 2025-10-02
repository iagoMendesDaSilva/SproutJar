package com.sproutjar.data.repositories

import com.sproutjar.R
import com.sproutjar.data.api.ApiService
import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.data.models.CdiRate
import com.sproutjar.utils.DateFormatPattern
import com.sproutjar.utils.DateService
import com.sproutjar.utils.ErrorService
import com.sproutjar.utils.Resource
import retrofit2.HttpException
import java.util.Date
import javax.inject.Inject


class Repository @Inject constructor(
    private val apiService: ApiService,
) : ErrorService {

    suspend fun getCdiHistoric(initialDate: Date): Resource<List<CdiRate>> {
        val formattedStart = DateService.formatDate(initialDate, DateFormatPattern.SELIC_DATE_HISTORIC)
        val formattedEnd = DateService.formatDate(Date(), DateFormatPattern.SELIC_DATE_HISTORIC)
        return request { apiService.getCdiHistoric(formattedStart, formattedEnd) }
    }

    suspend fun getCdiToday(): Resource<CdiRate> {
        return request { apiService.getCdiToday().first() }
    }

    private suspend fun <T> request(
        defaultMessage: MessageDialog = MessageDialog(
            R.string.error_internal_server,
            R.string.checking_server
        ),
        apiCall: suspend () -> T
    ): Resource<T> {
        return try {
            val response = apiCall.invoke()
            Resource.Success(response)
        } catch (e: HttpException) {
            Resource.Error(
                DialogInfo(getErrorMessage(e.code(), defaultMessage), e.code())
            )
        } catch (e: Exception) {
            Resource.Error(
                DialogInfo(
                    MessageDialog(
                        titleID = R.string.error_internal_server,
                        messageID = R.string.checking_server
                    ),
                    ErrorService.HTTP_500_INTERNAL_SERVER_ERROR
                )
            )
        }
    }
}
