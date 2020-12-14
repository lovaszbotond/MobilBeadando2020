package com.example.iforgotsomething.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://static.developer.riotgames.com/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface LolService {
    @GET("docs/lol/seasons.json")
    suspend fun getProperties(): List<LolProperty>
}

object LolApi {
    val retrofitService: LolService by lazy {
        retrofit.create(LolService::class.java)
    }
}