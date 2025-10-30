package com.example.matifood.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
//import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class Food(
    @SerializedName("_id") val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val image: String
): Serializable


data class Category (
    var imagePath : Int,
    var name : String
)

data class Order(
    @SerializedName("_id") val id: String,
    val userId: String,
    val items: List<Food>, // Giả sử items là một danh sách Food
    val amount: Double,
    val address: String, // Backend của bạn có 'address: { type: String }'
    val status: String,
    val date: String,
    val payment: Boolean
)

// --- Các lớp Request (Dùng để gửi đi) ---

data class LoginRequest(
    val email: String,
    val password: String
)

// Dùng cho API: /api/user/register
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// Dùng cho API: /api/cart/add và /api/cart/remove
data class CartItemRequest(
    val itemId: String
)

// Dùng cho API: /api/food/remove
data class RemoveFoodRequest(
    val id: String
)

// Dùng cho API: /api/order/place
data class PlaceOrderRequest(
    val items: List<Food>, // Hoặc một data class Item đơn giản hơn
    val amount: Double,
    val address: String // Backend của bạn có 'address: { type: String }'
)


// --- Các lớp Response (Dùng để nhận về) ---

// Dùng cho API: /api/user/login và /api/user/register
data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val token: String?
)

// Dùng cho API: /api/food/list
data class ListFoodResponse(
    val success: Boolean,
    val data: List<Food>
)

// Dùng cho API: /api/cart/get
//data class GetCartResponse(
//    val success: Boolean,
//    val cartData: Map<Food, Int> // Dựa trên userModel.js (cartData: { type: Object })
//)

// Dùng cho API: /api/order/place
data class PlaceOrderResponse(
    val success: Boolean,
    val session_url: String?,
    val message: String?
)

// Dùng cho API: /api/order/orderusers và /api/order/list
data class ListOrderResponse(
    val success: Boolean,
    val data: List<Order>?,
    val message: String?
)

// Dùng cho các phản hồi chung (Thêm/Xóa món, Thêm/Xóa giỏ hàng)
data class GenericResponse(
    val success: Boolean,
    val message: String
)
