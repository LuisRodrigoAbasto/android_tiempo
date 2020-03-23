package com.luis.rodrigo.abasto.android.mianuncio.mianuncio.repositorio.servidor

import android.database.Observable
import com.luis.rodrigo.abasto.android.mianuncio.mianuncio.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface OpenWeatherServicio {
    @GET("weather")
    fun tiempoActual(@Query("lat")latitud:Double,
                     @Query("lon") longitud: Double,
                     @Query("APPID") appid: String=BuildConfig.OPEN_WEATHER_API_KEY,
                     @Query("units") units: String = "metric",
                     @Query("lang") lang:String="es"):Observable<Modelos.ResultadoTiempoActual>
    companion object {
        fun crear(): OpenWeatherServicio {
val interceptor = HttpLoggingInterceptor()
            interceptor.level=HttpLoggingInterceptor.Level.BODY
            val client= OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.weather_api)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(OpenWeatherServicio::class.java)
        }
    }
}