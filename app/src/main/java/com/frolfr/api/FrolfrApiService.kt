package com.frolfr.api

import com.frolfr.api.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://frolfr.herokuapp.com/api/"

object FrolfrAuthorization {
    var authToken: String? = null
    var email: String? = null
}

val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(
        Interceptor { chain ->
            val builder = chain.request().newBuilder()
            if (FrolfrAuthorization.authToken != null && FrolfrAuthorization.email != null) {
                builder.header(
                    "Authorization",
                    "Token token=${FrolfrAuthorization.authToken},email=${FrolfrAuthorization.email}"
                )
            }
            return@Interceptor chain.proceed(builder.build())
        }
    )
}.build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface FrolfrApiService {
    @POST("authorizations")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("users/current")
    suspend fun currentUser(): UserResponse

    @GET("courses")
    suspend fun userCourses(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): UserCoursesResponse

    @GET("available_courses")
    suspend fun availableCourses(): AvailableCoursesResponse
}

object FrolfrApi {
    val retrofitService: FrolfrApiService by lazy {
        retrofit.create(FrolfrApiService::class.java)
    }
}
