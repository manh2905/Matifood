package com.example.matifood.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.matifood.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Giả sử bạn vẫn có object TokenManager để lưu token
object TokenManager {
    var authToken: String? = null
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        authToken = prefs.getString("auth_token", null)
    }

    fun saveToken(token: String) {
        authToken = token
        prefs.edit().putString("auth_token", token).apply()
    }

    fun clearToken() {
        authToken = null
        prefs.edit().remove("auth_token").apply()
    }

    fun getToken(): String? = authToken
}

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = TokenManager.getToken()

        // ✅ log debug xem token có null không
        Log.i("fix","🔑 Sending token: $token")

        val requestBuilder = originalRequest.newBuilder()
        if (!token.isNullOrEmpty()) {
            // ✅ header name phải chính xác: "token"
            requestBuilder.addHeader("token", token)
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}

object RetrofitClient {


    private const val BASE_URL = "http://10.0.2.2:5000/"

    // Tạo OkHttpClient với Interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    // Khởi tạo Retrofit
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <-- Sử dụng OkHttpClient tùy chỉnh
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}