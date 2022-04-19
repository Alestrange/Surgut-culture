package ru.alestrange.cultureSurgut.serverDownload

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.internal.Version
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.alestrange.cultureSurgut.data.*

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
    @Headers("Cache-Control: no-cache")
    @GET("surgut_culture_version.json")
    suspend fun getVersion(): SurgutCultureVersion
    @Headers("Cache-Control: no-cache")
    @GET("interest.json")
    suspend fun getInterest(): List<Interest>
    @Headers("Cache-Control: no-cache")
    @GET("history.json")
    suspend fun getHistory(): List<History>
    @Headers("Cache-Control: no-cache")
    @GET("cultobject.json")
    suspend fun getCultobject(): List<Cultobject>
    @Headers("Cache-Control: no-cache")
    @GET("cultobject_tag.json")
    suspend fun getCultobjectTag(): List<CultobjectTag>
    @Headers("Cache-Control: no-cache")
    @GET("tag.json")
    suspend fun getTag(): List<Tag>
    @Headers("Cache-Control: no-cache")
    @GET("illustration.json")
    suspend fun getIllustration(): List<Illustration>
    @Headers("Cache-Control: no-cache")
    @GET("cultobject_illustration.json")
    suspend fun getCultobjectIllustration(): List<CultobjectIllustration>
    @Headers("Cache-Control: no-cache")
    @GET("cultobject_history.json")
    suspend fun getCultobjectHistory(): List<CultobjectHistory>
    @Headers("Cache-Control: no-cache")
    @GET("history_illustration.json")
    suspend fun getHistoryIllustration(): List<HistoryIllustration>
    @Headers("Cache-Control: no-cache")
    @GET("cycleroute.json")
    suspend fun getCycleRoute(): List<CycleRoute>
    @Headers("Cache-Control: no-cache")
    @GET("cycle_checkpoint.json")
    suspend fun getCycleCheckpoint(): List<CycleCheckpoint>
    @Headers("Cache-Control: no-cache")
    @GET("cultobject_cycleroute.json")
    suspend fun getCultobjectCycleroute(): List<CultobjectCycleroute>
    @Headers("Cache-Control: no-cache")
    @GET("link.json")
    suspend fun getLink(): List<Link>
}

object WebApi {
    val retrofitService : WebApiService by lazy {
        retrofit.create(WebApiService::class.java) }
}
