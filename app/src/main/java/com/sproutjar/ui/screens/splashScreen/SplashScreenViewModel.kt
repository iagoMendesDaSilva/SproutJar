package com.sproutjar.ui.screens.splashScreen

import androidx.lifecycle.ViewModel
import com.sproutjar.data.models.SelicTax
import com.sproutjar.data.repositories.Repository
import com.sproutjar.utils.DateFormatPattern
import com.sproutjar.utils.DateService
import com.sproutjar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _selicToday = MutableStateFlow<Resource<SelicTax>>(Resource.Loading())
    val selicToday = _selicToday.asStateFlow()

    suspend fun fetchSelicToday() {
        _selicToday.value = Resource.Loading()

        val result = repository.getSelicTaxToday()
        _selicToday.value = when (result) {
            is Resource.Success -> Resource.Success(result.data!!)
            is Resource.Error -> Resource.Error(result.dialogInfo)
            else -> Resource.Error()
        }
    }
}