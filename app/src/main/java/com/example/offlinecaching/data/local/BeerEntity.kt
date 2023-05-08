package com.example.offlinecaching.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BeerEntity(
    val name: String,
    @PrimaryKey
    val id: Int,
    val tagline: String,
    val description: String,
    val firstBrewed: String,
    val imageUrl: String?
)
