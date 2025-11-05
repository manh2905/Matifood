package com.example.matifood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.auth.TokenManager
import com.example.matifood.models.CartItemRequest
import com.example.matifood.models.Food
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.remove
import kotlin.compareTo
import kotlin.text.get
import kotlin.text.set


class CartViewModel : ViewModel() {

    private val api = RetrofitClient.instance

    private var updateJob: Job? = null


    private val _cartState = MutableStateFlow<CartState>(CartState.Idle)
    val cartState: StateFlow<CartState> = _cartState


    private val _cartData = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartData: StateFlow<Map<String, Int>> = _cartData


    /**  Lấy giỏ hàng người dùng từ API */
    fun fetchCart() {
        viewModelScope.launch {
            try {
                _cartState.value = CartState.Loading
                Log.i("fixx", TokenManager.getToken().toString())
                val response = api.getCart()
                Log.i("fix", response.body()?.cartData.toString())
                if (response.isSuccessful && response.body()?.success == true) {
                    _cartData.value = response.body()!!.cartData
                    _cartState.value = CartState.Success
                } else {
                    _cartState.value = CartState.Error(
                        "Không thể tải giỏ hàng"
                    )
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }


    /** ➕ Thêm món vào giỏ */
    fun addToCart(itemId: String) {
        viewModelScope.launch {
            try {
                _cartState.value = CartState.Loading
                val response = api.addToCart(CartItemRequest(itemId))
                if (response.isSuccessful && response.body()?.success == true) {
                    fetchCart() // cập nhật lại giỏ
                } else {
                    _cartState.value = CartState.Error("Thêm món thất bại")
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error("Lỗi: ${e.message}")
            }
        }
    }


    /** ➖ Xóa món khỏi giỏ */
    fun removeFromCart(itemId: String) {
        viewModelScope.launch {
            try {
                _cartState.value = CartState.Loading
                val response = api.removeFromCart(CartItemRequest(itemId))
                if (response.isSuccessful && response.body()?.success == true) {
                    fetchCart()
                } else {
                    _cartState.value = CartState.Error("Xóa món thất bại")
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error("Lỗi: ${e.message}")
            }
        }
    }

    /** 🧹 Xóa dữ liệu cục bộ (khi đăng xuất hoặc refresh) */
    fun clearLocalCart() {
        _cartData.value = emptyMap()
        _cartState.value = CartState.Idle
    }

    fun changeQuantity(foodId: String, delta: Int) {
        // Cập nhật UI trước
        _cartData.value = _cartData.value.toMutableMap().apply {
            val newQty = (this[foodId] ?: 0) + delta
            if (newQty <= 0) remove(foodId)
            else this[foodId] = newQty
        }

        // Huỷ job cũ (nếu người dùng bấm nhanh)
        updateJob?.cancel()

        // Đợi 1000ms, nếu không bấm tiếp thì mới gọi API
        updateJob = viewModelScope.launch {
            delay(1000)
            if (delta > 0)
                RetrofitClient.instance.addToCart(CartItemRequest(foodId))
            else
                RetrofitClient.instance.removeFromCart(CartItemRequest(foodId))
        }
    }
}





/** Trạng thái phản hồi của ViewModel */
sealed class CartState {
    object Idle : CartState()
    object Loading : CartState()
    object Success : CartState()
    data class Error(val message: String) : CartState()
}
