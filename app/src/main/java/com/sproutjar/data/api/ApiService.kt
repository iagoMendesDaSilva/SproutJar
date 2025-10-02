package com.sproutjar.data.api

import com.sproutjar.data.models.CdiRate
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton
@Singleton
interface ApiService {

    companion object {
        internal const val BASE_URL = "https://api.bcb.gov.br/dados/serie/"
        const val CDI_HISTORIC = "bcdata.sgs.4389/dados?formato=json"
        const val CDI_LAST_DAY = "bcdata.sgs.4389/dados/ultimos/1?formato=json"
    }


    @GET(CDI_HISTORIC)
    suspend fun getCdiHistoric(
        @Query("dataInicial") initialDate: String,
        @Query("dataFinal") endDate: String
    ): List<CdiRate>

    @GET(CDI_LAST_DAY)
    suspend fun getCdiToday(): List<CdiRate>
}
