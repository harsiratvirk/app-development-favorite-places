package com.example.attractiveplaces.nettverk

import com.example.attractiveplaces.data.Place
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://dave3600.cs.oslomet.no/~hakau9101/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("get_places.php")
    suspend fun getPlaces(): List<Place>

    @FormUrlEncoded
    @POST("add_place.php")
    suspend fun addPlace(
        @Field("name") name: String,
        @Field("description") description: String?,
        @Field("streetAddress") streetAddress: String?,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    )
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
