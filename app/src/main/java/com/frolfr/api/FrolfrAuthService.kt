package com.frolfr.api

import com.frolfr.BuildConfig
import com.frolfr.api.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "${BuildConfig.BASE_URL}/"

object FrolfrAuthorization {
    var authToken: String? = null
    var email: String? = null
    var userId: Int? = null
}

val okHttpAuthClient: OkHttpClient = OkHttpClient.Builder().apply {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    addInterceptor(loggingInterceptor)
}.build()

private val moshiJson = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpAuthClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshiJson))
    .build()

interface FrolfrAuthService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}

object FrolfrAuth {
    val retrofitService: FrolfrAuthService by lazy {
        retrofit.create(FrolfrAuthService::class.java)
    }
}