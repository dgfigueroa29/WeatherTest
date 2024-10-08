package com.boa.data.datasource.remote

import com.boa.data.util.BASE_URL
import com.boa.domain.base.BaseError
import com.boa.domain.base.BaseException
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Calendar
import javax.net.ssl.HttpsURLConnection

class ApiProvider {
    var api: AppApi? = null

    init {
        try {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Calendar::class.java, object : JsonDeserializer<Calendar> {
                override fun deserialize(
                    json: JsonElement,
                    typeOf: Type,
                    context: JsonDeserializationContext
                ): Calendar {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = json.asJsonPrimitive.asLong * 1000
                    return calendar
                }
            })

            val okHttpClient = getOkHttpClient()
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build()
            api = retrofitBuilder.create(AppApi::class.java)
        } catch (e: Exception) {
            Timber.e("ApiErrorInit: ${e.localizedMessage}")
        }
    }

    @Suppress("KotlinConstantConditions")
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        try {
            builder.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                requestBuilder.addHeader("Accept", "application/json")
                requestBuilder.addHeader("Content-type", "application/json")
                chain.proceed(requestBuilder.build())
            }

            builder.addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                if (response.code > HttpsURLConnection.HTTP_ACCEPTED) {
                    val parser = Gson()

                    response.body?.let {
                        var error = BaseError(code = "${response.code}")
                        try {
                            error = parser.fromJson(it.charStream(), BaseError::class.java)
                        } catch (_: Exception) {
                        } finally {
                            throw BaseException(error)
                        }
                    }
                }

                response
            }

            //Only for debug for checking the api connection
            if (BuildConfig.BUILD_TYPE == "debug") {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.HEADERS
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }
        } catch (e: Exception) {
            Timber.e("ApiError: ${e.localizedMessage}")
        }

        return builder.build()
    }
}