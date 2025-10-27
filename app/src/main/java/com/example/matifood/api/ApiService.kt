package com.example.matifood.api

import com.example.matifood.models.AuthResponse
import com.example.matifood.models.CartItemRequest
import com.example.matifood.models.GenericResponse
import com.example.matifood.models.GetCartResponse
import com.example.matifood.models.ListFoodResponse
import com.example.matifood.models.ListOrderResponse
import com.example.matifood.models.LoginRequest
import com.example.matifood.models.PlaceOrderRequest
import com.example.matifood.models.PlaceOrderResponse
import com.example.matifood.models.RegisterRequest
import com.example.matifood.models.RemoveFoodRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @POST("api/user/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/user/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>


    @GET("api/food/list")
    suspend fun listFood(): Response<ListFoodResponse>

    // --- Food (Endpoint "add" là MULTIPART, cần Auth) ---
    @Multipart // Đánh dấu đây là request Multipart
    @POST("api/food/add")
    suspend fun addFood(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part // Đây là tệp tin ảnh
    ): Response<GenericResponse>

    // --- Food (Endpoint "remove" dùng DELETE có body, cần Auth) ---
    @HTTP(method = "DELETE", path = "api/food/remove", hasBody = true)
    suspend fun removeFood(@Body request: RemoveFoodRequest): Response<GenericResponse>

    // --- Cart (Tất cả đều cần Auth) ---
    @POST("api/cart/add")
    suspend fun addToCart(@Body request: CartItemRequest): Response<GenericResponse>

    @POST("api/cart/remove")
    suspend fun removeFromCart(@Body request: CartItemRequest): Response<GenericResponse>

    // API "getCart" của bạn dùng POST (theo cartRoute.js) và không cần body
    @POST("api/cart/get")
    suspend fun getCart(): Response<GetCartResponse>

    // --- Order (Tất cả đều cần Auth, trừ verify và list) ---
    @POST("api/order/place")
    suspend fun placeOrder(@Body request: PlaceOrderRequest): Response<PlaceOrderResponse>

    @POST("api/order/orderusers")
    suspend fun getUserOrders(): Response<ListOrderResponse>

    // Các API không cần Auth
    @POST("api/order/list")
    suspend fun listAllOrders(): Response<ListOrderResponse>
}