package com.example.stockmarketapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.stockmarketapp.presentation.company_info.CompanyInfoScreen
import com.example.stockmarketapp.presentation.company_listing.CompanyListingScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = NavRouts.CompanyListingsScreen) {

        composable<NavRouts.CompanyListingsScreen> {
            CompanyListingScreen(navController)
        }

        composable<NavRouts.CompanyInfoScreen> { backEntry ->
            val arg = backEntry.toRoute<NavRouts.CompanyInfoScreen>()
            CompanyInfoScreen(symbol = arg.symbol)
        }
    }
}