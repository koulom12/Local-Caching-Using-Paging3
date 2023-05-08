package com.example.offlinecaching.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.offlinecaching.data.local.BeerDatabase
import com.example.offlinecaching.data.local.BeerEntity
import kotlinx.coroutines.delay
import retrofit2.HttpException
import toBeerEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BeerRemoteMediator(
    private val beerDB: BeerDatabase,
    private val beerApi: BeerApi
) : RemoteMediator<Int, BeerEntity>(){

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BeerEntity>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            delay(2000)
            val beers = beerApi.getBeers(page = loadKey, pageCount = state.config.pageSize)

            beerDB.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    beerDB.dao.clearAll()
                }
                val beerEntities = beers.map { it.toBeerEntity() }
                beerDB.dao.upsertAll(beerEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = beers.isEmpty()
            )
        } catch (e: IOException){
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}