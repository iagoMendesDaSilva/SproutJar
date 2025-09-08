package com.sproutjar.data

import com.sproutjar.data.models.SelicTax
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface ApiService {

    companion object {
        internal const val BASE_URL = "https://api.bcb.gov.br/dados/serie/"
        const val HISTORIC = "bcdata.sgs.12/dados?formato=json"
        const val LAST_DAY = "bcdata.sgs.12/dados/ultimos/1?formato=json"
    }

    @GET(HISTORIC)
    suspend fun getSelicTaxHistoric(
        @Query("dataInicial") initialDate: String,
        @Query("dataFinal") endDate: String
    ): List<SelicTax>

    @GET(LAST_DAY)
    suspend fun getSelicTaxToday(
    ): List<SelicTax>


}