package com.example.stockmarketapp.data.repository

import android.util.Log
import coil.network.HttpException
import com.example.stockmarketapp.data.csv.CSVParser
import com.example.stockmarketapp.data.local.StockDatabase
import com.example.stockmarketapp.data.mapper.toCompanyInfo
import com.example.stockmarketapp.data.mapper.toCompanyListEntity
import com.example.stockmarketapp.data.mapper.toCompanyListing
import com.example.stockmarketapp.data.remote.StockApi
import com.example.stockmarketapp.domain.model.CompanyInfo
import com.example.stockmarketapp.domain.model.CompanyListing
import com.example.stockmarketapp.domain.model.IntradayInfo
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>,
) : StockRepository {
    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<CompanyListing>>> {

        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            Log.d("RoomStatus", "localListings: ${localListings.size}")
            emit(
                Resource.Success(
                data = localListings.map { it.toCompanyListing() }))
            val isDbEmpty = localListings.isEmpty()
            Log.d("RoomStatus", "isDbEmpty: $isDbEmpty")
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            Log.d("RoomStatus", "shouldJustLoadFromCache: $shouldJustLoadFromCache")
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                //to parse from CSV to CompanyListing
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                Log.d("RoomStatus", "remoteListings: ${listings.size}")

                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListEntity() })
                emit(
                    Resource.Success(
                    data = dao.searchCompanyListing("").map { it.toCompanyListing() }))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response=api.getIntradayInfo(symbol)
            val result =intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Could not load intraday Info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Could not load intraday Info")
        }

    }
    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val response = api.getCompanyInfo(symbol)
            val result =response.toCompanyInfo()
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Could not load Company Info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Could not load Company Info")
        }
    }


}