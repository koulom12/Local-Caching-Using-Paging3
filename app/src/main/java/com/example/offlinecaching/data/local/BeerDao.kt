package com.example.offlinecaching.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface BeerDao {

    @Upsert
    suspend fun upsertAll(beers: List<BeerEntity>)

    @Query("SELECT * FROM BeerEntity")
    fun pagingSource(): PagingSource<Int, BeerEntity>

    @Query("DELETE FROM BeerEntity")
    suspend fun clearAll()
}