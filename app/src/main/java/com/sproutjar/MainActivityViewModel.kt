package com.sproutjar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sproutjar.data.database.PotDao
import com.sproutjar.data.database.TransactionDao
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.Pot
import com.sproutjar.data.models.PotUI
import com.sproutjar.data.models.SelicTax
import com.sproutjar.data.models.Transaction
import com.sproutjar.data.repositories.Repository
import com.sproutjar.utils.AppPreference
import com.sproutjar.utils.ErrorService
import com.sproutjar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appPreference: AppPreference,
    private val repository: Repository,
    private val potDao: PotDao,
    private val transactionDao: TransactionDao,
) : ViewModel() {

    val appSettings: StateFlow<AppSettings> = appPreference.appSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AppSettings())

    private val _cdiToday = MutableStateFlow<Resource<SelicTax>>(Resource.Loading())
    val cdiToday = _cdiToday.asStateFlow()

    private val _cdiHistory = MutableStateFlow<Resource<List<SelicTax>>>(Resource.Loading())
    val cdiHistory = _cdiHistory.asStateFlow()

    private val _pots = MutableStateFlow<List<PotUI>>(emptyList())
    val pots: StateFlow<List<PotUI>> = _pots.asStateFlow()

    fun saveAppSettings(appSettings: AppSettings) {
        viewModelScope.launch {
            appPreference.saveAppSettings(appSettings)
        }
    }

    suspend fun fetchPots() {
        val potsList = potDao.getPots()
        val potUIList = potsList.map { pot ->
            val transactions = transactionDao.getTransactionsForPot(pot.id)
            PotUI(pot, transactions)
        }
        _pots.value = potUIList
    }

    suspend fun insertPot(pot: Pot) {
        potDao.insertPot(pot)
        fetchPots()
    }

    suspend fun deletePot(pot: Pot) {
        potDao.deletePot(pot)
        fetchPots()
    }

    suspend fun updatePot(pot: Pot) {
        potDao.updatePot(pot)
        fetchPots()
    }

    suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
        fetchPots()
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
        fetchPots()
    }

    suspend fun fetchCdiToday() {
        _cdiToday.value = Resource.Loading()

        val result = repository.getCdiToday()
        _cdiToday.value = when (result) {
            is Resource.Success -> Resource.Success(result.data!!)
            is Resource.Error -> Resource.Error(result.dialogInfo)
            else -> Resource.Error()
        }
    }

    suspend fun fetchCdiHistory() {
        _cdiHistory.value = Resource.Loading()
        val oldestTransactionDate = getOldestTransactionDate()
        val result = repository.getCdiHistoric(oldestTransactionDate)
        _cdiHistory.value = when (result) {
            is Resource.Success -> Resource.Success(result.data!!)
            is Resource.Error -> {
                if (result.dialogInfo.error == ErrorService.HTTP_404_NOT_FOUND)
                    Resource.Success(emptyList())
                else
                    Resource.Error(result.dialogInfo)
            }
            else -> Resource.Error()
        }
    }

    private fun getOldestTransactionDate(): Date {
        return pots.value
            .flatMap { it.transactions }
            .minOfOrNull { it.date } ?: Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }.time
    }

}