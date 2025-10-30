package com.example.matifood.viewmodel

import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

//    val cartData = MutableLiveData<Map<Food, Int>>()

//    fun fetchCart() {
//        viewModelScope.launch {
//            try {
//                // Không cần truyền token hay userId, Interceptor sẽ lo
//                val response = RetrofitClient.instance.getCart()
//
//                if (response.isSuccessful && response.body()?.success == true) {
//                    // Lấy được giỏ hàng
//                    cartData.postValue(response.body()?.cartData )
//                } else {
//                    // TODO: Xử lý lỗi (ví dụ: token hết hạn)
//                }
//            } catch (e: Exception) {
//                // TODO: Xử lý lỗi mạng
//            }
//        }
//    }
}