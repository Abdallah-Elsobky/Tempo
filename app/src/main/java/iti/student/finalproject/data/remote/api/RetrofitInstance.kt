package iti.student.finalproject.data.remote.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.openweathermap.org/"
    private const val API_KEY = "cf398e3fa15c308d85df324c69b3a81d"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val originalRequest = chain.request()

            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("appid", API_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    val api: WeatherApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(WeatherApiService::class.java)
    }
}