package com.example.stockmarketapp.presentation.company_listing


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.stockmarketapp.presentation.navigation.NavRouts
import com.example.stockmarketapp.presentation.ui_util.SimpleSearchBar

@Composable
fun CompanyListingScreen(
    navigator: NavController,
    viewModel: CompanyListingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    Log.d("stateDebugging","companies: ${state.companies.size} , isLoading: ${state.isLoading},  searchQuery: ${state.searchQuery}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
    // 🔍 Search bar
        SimpleSearchBar(
            textFieldState = state.searchQuery,
            onQueryChange = {
                viewModel.onEvent(CompanyListingsEvent.OnSearchQueryChange(it))
            },
            searchResults = emptyList()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 🔄 Pull-to-refresh container
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onEvent(CompanyListingsEvent.Refresh) },
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullToRefreshState
                )
            }
        ) {
            Log.d("CompanyListingScreen","state.companies: ${state.companies.size}")
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.companies) { company ->
                    Log.d("CompanyListingScreen",": ${company.name}")
                  //  val company = state.companies[company]
                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                               navigator.navigate(NavRouts.CompanyInfoScreen(company.symbol))
                            }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
