package com.example.offlinecaching.data.remote

data class BeerDto(
    val name: String,
    val id: Int,
    val tagline: String,
    val description: String,
    val first_brewed: String,
    val image_url: String?
)
