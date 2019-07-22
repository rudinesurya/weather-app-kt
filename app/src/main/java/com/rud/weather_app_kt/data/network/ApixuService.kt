package com.rud.weather_app_kt.data.network

import com.rud.weather_app_kt.data.network.response.CurrentWeatherResponse
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.apixu.com/v1/current.json?key=7ef4b921308c4a21b7a144121192107&q=Paris
interface ApixuService {
    @GET("/v1/current.json")
    fun getCurrentWeather(
        @Query("q") location: String
    ): Observable<CurrentWeatherResponse>

    companion object {
        operator fun invoke(
            apiKey: String,
            connectivityInterceptor: ConnectivityInterceptor
        ): ApixuService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", apiKey)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.apixu.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApixuService::class.java)
        }
    }
}