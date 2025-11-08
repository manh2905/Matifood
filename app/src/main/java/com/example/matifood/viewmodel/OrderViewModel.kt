package com.example.matifood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.models.OrderResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val api = RetrofitClient.instance

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders

    /**
     * Gọi API để lấy danh sách đơn hàng theo userId
     */
    fun fetchOrders() {
        viewModelScope.launch {
            try {
                _orderState.value = OrderState.Loading

                val response = api.getUserOrders()
                if (response.isSuccessful && response.body()?.success == true) {
                    _orders.value = response.body()?.data!!
                    _orderState.value = OrderState.Success
                    Log.i("OrderVM", " Loaded ${_orders.value.size} orders")
                } else {
                    _orderState.value = OrderState.Error("Không thể tải đơn hàng")
                    Log.e("OrderVM", " Response error: ${response.code()}")
                }

            } catch (e: Exception) {
                _orderState.value = OrderState.Error("Lỗi: ${e.message}")
                Log.e("OrderVM", " Exception: ${e.message}")
            }
        }
    }
}

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    object Success : OrderState()
    data class Error(val message: String) : OrderState()
}
