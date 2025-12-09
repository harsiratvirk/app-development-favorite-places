package com.example.attractiveplaces.data

import com.squareup.moshi.Json

data class Place(
    val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String?,
    @Json(name = "streetAddress") val streetAddress: String?,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
)
