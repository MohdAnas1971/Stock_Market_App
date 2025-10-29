package com.example.stockmarketapp.presentation.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class NavRouts(){
    @Serializable
    object CompanyListingsScreen: NavRouts()

    @Serializable
    data class CompanyInfoScreen(val symbol: String): NavRouts()

}