package ru.alestrange.cultureSurgut.serverDownload

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.internal.Version
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import ru.alestrange.cultureSurgut.data.History
import ru.alestrange.cultureSurgut.data.Interest
import ru.alestrange.cultureSurgut.data.SurgutCultureVersion
import ru.alestrange.cultureSurgut.data.Tag

private const val BASE_URL =
    "https://raw.githubusercontent.com/Alestrange/Surgut-culture/master/app/src/main/res/raw/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WebApiService {
    @GET("surgut_culture_version.json")
    suspend fun getVersion(): SurgutCultureVersion
    @GET("interest.json")
    suspend fun getInterest(): List<Interest>
    @GET("history.json")
    suspend fun getHistory(): List<History>
    @GET("tag.json")
    suspend fun getTag(): List<Tag>
}

object WebApi {
    val retrofitService : WebApiService by lazy {
        retrofit.create(WebApiService::class.java) }
}
