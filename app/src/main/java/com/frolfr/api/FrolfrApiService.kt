package com.frolfr.api

import com.frolfr.BuildConfig
import com.frolfr.api.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import moe.banana.jsonapi2.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.*

private const val BASE_URL = "${BuildConfig.BASE_URL}/jsonapi/"

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
                    "Bearer ${FrolfrAuthorization.authToken}"
                )
            }
            builder.addHeader("Content-Type", "application/vnd.api+json")
            return@Interceptor chain.proceed(builder.build())
        }
    )

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }
    addInterceptor(loggingInterceptor)
}.build()

private val jsonApiAdapterFactory = ResourceAdapterFactory.builder()
    .add(Round::class.java)
    .add(User2::class.java)
    .add(Course2::class.java)
    .add(Default::class.java)
    .build()

private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
    .add(jsonApiAdapterFactory)
    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(JsonApiConverterFactory.create(moshi))
    .build()

interface FrolfrApiService {
    @POST("${BuildConfig.BASE_URL}/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("users")
    suspend fun currentUser(
        @Query("me") me: Boolean = true
    ): User2

    @GET("rounds")
    suspend fun rounds(
        @Query("page[number]") page: Int,
        @Query("page[size]") perPage: Int,
        @Query("include") includes: String
    ): Document /*ArrayDocument<Round>*/ /*UserCoursesResponse*/

    @GET("available_courses")
    suspend fun availableCourses(): AvailableCoursesResponse

    @GET("course_scorecards")
    suspend fun userCourseScorecards(
        @Query("course_id") courseId: Int
    ): UserCourseScorecardsResponse

    @GET("courses/{courseId}")
    suspend fun course(
        @Path("courseId") courseId: Int
    ): CourseResponse

    @GET("rounds/{roundId}")
    suspend fun scorecard(
        @Path("roundId") scorecardId: Int
    ): ScorecardResponse

    @GET("users/{userId}")
    suspend fun user(
        @Path("userId") userId: Int
    ): UserResponse

    @GET("friends")
    suspend fun friends(): FriendsResponse
}

object FrolfrApi {
    val retrofitService: FrolfrApiService by lazy {
        retrofit.create(FrolfrApiService::class.java)
    }
}