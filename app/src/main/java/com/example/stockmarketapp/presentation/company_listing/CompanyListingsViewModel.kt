package com.example.stockmarketapp.presentation.company_listing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository,
) : ViewModel() {
   private val _state = MutableStateFlow(CompanyListingsState())
    val  state =_state.asStateFlow()
    private var searchJob: Job? = null
    init {
        getCompanyListings("",true)
    }
    fun onEvent(event: CompanyListingsEvent) {

        when (event) {
            is CompanyListingsEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }

            is CompanyListingsEvent.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }
    private fun getCompanyListings(
        query: String = _state.value.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false,
    ) {
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote = fetchFromRemote, query = query)
                .collect { result ->
                    when (result) {

                        is Resource.Success -> {
                            result.data?.let { listings ->
                                Log.d("CompanyListingsViewModel", "Success: ${listings.size}")
                                _state.value =
                                    _state.value.copy(companies = listings, isLoading = false)
                            }
                        }
                        is Resource.Error -> {
                            Log.d("CompanyListingsViewModel","Error getCompanyListings")
                        }
                        is Resource.Loading -> {
                            Log.d("CompanyListingsViewModel","Loading getCompanyListings")
                          _state.value= _state.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}