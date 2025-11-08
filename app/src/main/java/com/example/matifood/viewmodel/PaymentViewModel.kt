package com.example.matifood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.models.MobileOrderRequest
import com.example.matifood.models.OrderItem
import com.example.matifood.models.VerifyOrderRequest
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {

    private val api = RetrofitClient.instance
    var currentOrderId: String? = null
        private set

    fun createPaymentIntent(
        amount: Double,
        items: List<OrderItem>,
        address: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = MobileOrderRequest(
                    items = items,
                    amount = amount,
                    address = address
                )

                val response = api.placeOrderMobile(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    val body = response.body()!!
                    currentOrderId = body.orderId //  Lưu orderId
                    Log.i("StripePayment", "API trả về: $body")
                    Log.d("StripeDebug", " ClientSecret: ${body.clientSecret}")

                    onResult(body.clientSecret)
                } else {
                    Log.e("StripePayment", " API thất bại: ${response.errorBody()?.string()}")
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e("StripePayment", " Lỗi gọi API: ${e.message}", e)
                onResult(null)
            }
        }
    }

    fun verifyOrder(orderId: String?, success: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.verifyOrder(
                    VerifyOrderRequest(orderId, success.toString())
                )

                Log.i("VerifyDebug", "📡 orderId=$orderId, success=$success")
                Log.i("VerifyDebug", "📩 Response code=${response.code()}, body=${response.body()}, error=${response.errorBody()?.string()}")
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.i("VerifyOrder", "✅ Đơn hàng xác nhận thành công")
                    onResult(true)
                } else {
                    Log.e("VerifyOrder", "❌ Xác nhận thất bại: ${response.body()?.message}")
                    onResult(false)
                }
            } catch (e: Exception) {
                Log.e("VerifyOrder", "⚠️ Lỗi: ${e.message}")
                onResult(false)
            }
        }
    }
}
