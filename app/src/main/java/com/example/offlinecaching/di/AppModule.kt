package com.example.offlinecaching.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.offlinecaching.data.local.BeerDatabase
import com.example.offlinecaching.data.local.BeerEntity
import com.example.offlinecaching.data.remote.BeerApi
import com.example.offlinecaching.data.remote.BeerApi.Companion.BASE_URL
import com.example.offlinecaching.data.remote.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBeerDataBase(@ApplicationContext context: Context) : BeerDatabase {
        return Room.databaseBuilder(context, BeerDatabase::class.java, "beers.db").build()
    }

    @Provides
    @Singleton
    fun provideBeerApi() : BeerApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun provideBeerPager(beerDb: BeerDatabase, beerApi: BeerApi) : Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(20),
            remoteMediator = BeerRemoteMediator(
                beerDB = beerDb,
                beerApi = beerApi
            ),
            pagingSourceFactory = {
                beerDb.dao.pagingSource()
            }

        )
    }
}