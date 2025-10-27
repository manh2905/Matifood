package com.example.matifood.auth

import com.example.matifood.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Giả sử bạn vẫn có object TokenManager để lưu token
object TokenManager { var authToken: String? = null }

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Lấy request gốc
        val originalRequest = chain.request()

        // Lấy token
        val token = TokenManager.authToken

        // Nếu không có token, cứ gửi request đi
        if (token == null) {
            return chain.proceed(originalRequest)
        }
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("token", token) // Key là "token"

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

object RetrofitClient {


    private const val BASE_URL = "http://10.0.2.2:4000/"

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