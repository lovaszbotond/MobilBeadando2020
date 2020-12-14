package com.example.iforgotsomething.network

import com.squareup.moshi.Json

class LolProperty (
    @Json(name = "id")
    val apiId: Int,
    val season: String
)