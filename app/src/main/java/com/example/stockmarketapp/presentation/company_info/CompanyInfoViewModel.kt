package com.example.stockmarketapp.presentation.company_info

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
): ViewModel(){

 var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbols = savedStateHandle.get<String>("symbol") ?: return@launch
            Log.d("CompanyInfoViewModel","Symbol: $symbols")
            state = state.copy(isLoading = true)
            val companyInfoResult = async {  repository.getCompanyInfo(symbols) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbols) }

            when(val result=companyInfoResult.await()){
                is Resource.Error-> {
                    state =state.copy(
                        company=result.data,
                        error =result.message,
                        isLoading = false,
                    )
                }
                is Resource.Success -> {
                    state =state.copy(
                        company=result.data,
                        //isLoading = false,
                     //   error = null
                    )
                }
                else -> Unit
            }

            when(val result=intradayInfoResult.await()){
                is Resource.Error -> {
                    state =state.copy(
                      company = null,
                        error =result.message,
                        isLoading = false,
                    )
                }
                is Resource.Success -> {
                    state =state.copy(
                        stockInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                else -> Unit
            }
        }
    }

}