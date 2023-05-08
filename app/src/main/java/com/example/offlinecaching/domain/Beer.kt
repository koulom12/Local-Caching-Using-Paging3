package com.example.offlinecaching.domain

data class Beer(
    val name: String,
    val id: Int,
    val tagline: String,
    val description: String,
    val firstBrewed: String,
    val imageUrl: String?
)
