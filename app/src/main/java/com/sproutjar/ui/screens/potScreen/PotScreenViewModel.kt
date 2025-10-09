package com.sproutjar.ui.screens.potScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sproutjar.data.database.TransactionDao
import com.sproutjar.data.models.Transaction
import com.sproutjar.data.repositories.Repository
import com.sproutjar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PotScreenViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
) : ViewModel() {

    private val _transactions = MutableStateFlow<Resource<List<Transaction>>>(Resource.Loading())
    val transactions: StateFlow<Resource<List<Transaction>>> = _transactions.asStateFlow()


    fun fetchTransactions(potID: Int) {
        viewModelScope.launch {
            _transactions.value = Resource.Loading()
            val transactionList = transactionDao.getTransactionsForPot(potID)
            _transactions.value = Resource.Success(transactionList)
        }
    }
}