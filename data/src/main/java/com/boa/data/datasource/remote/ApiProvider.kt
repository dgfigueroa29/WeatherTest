package com.boa.data.datasource.remote

import android.annotation.SuppressLint
import com.boa.data.BuildConfig
import com.boa.data.util.BASE_URL
import com.boa.domain.base.BaseError
import com.boa.domain.base.BaseException
import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class ApiProvider {
    val api: AppApi

    init {
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
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
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
                    } catch (ex: Exception) {
                    } finally {
                        throw BaseException(error)
                    }
                }
            }

            response
        }

        //Only for debug for checking the api connection
        @Suppress("ConstantConditionIf")
        if (BuildConfig.BUILD_TYPE == "debug") {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            }
        )

        //Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        //Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true }
        return builder.build()
    }
}