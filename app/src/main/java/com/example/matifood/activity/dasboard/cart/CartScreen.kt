package com.example.matifood.activity.dasboard.cart

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.matifood.models.Food
import com.example.matifood.viewmodel.AuthViewModel
import com.example.matifood.viewmodel.CartState
import com.example.matifood.viewmodel.CartViewModel
import com.example.matifood.viewmodel.FoodViewModel

@Composable
fun CartScreen(
    authVM: AuthViewModel,
    cartVM: CartViewModel,
    foodVM: FoodViewModel
) {
    val isLogin by authVM.isLoggedIn.collectAsState()
    val cartState by cartVM.cartState.collectAsState()
    val cartData by cartVM.cartData.collectAsState()

    // Danh sách món ăn trong giỏ hàng
    var cartFoods by remember { mutableStateOf<List<Food>>(emptyList()) }
    var isLoadingFoods by remember { mutableStateOf(false) }


    LaunchedEffect(isLogin) {
        if (isLogin) {
            cartVM.fetchCart()
        }
    }


    LaunchedEffect(cartData) {
        if (cartData.isNotEmpty()) {
            val ids = cartData.keys.toList()
            isLoadingFoods = true
            Log.i("CartScreen", "🛒 Fetching foods for cart IDs: $ids")
            foodVM.fetchFoodsByIds(ids) { foods ->
                if (foods != null) {
                    cartFoods = foods
                    Log.i("CartScreen", "✅ Loaded ${foods.size} foods for cart")
                } else {
                    Log.e("CartScreen", "❌ Không tải được danh sách món ăn")
                }
                isLoadingFoods = false
            }
        } else {
            cartFoods = emptyList()
        }
    }

    /**
     * B3: Render UI
     */
    when {
        !isLogin -> {
            Text(
                text = "Vui lòng đăng nhập để xem giỏ hàng.",
                modifier = Modifier.padding(24.dp),
                color = Color.Gray
            )
        }

        cartState is CartState.Loading || isLoadingFoods -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        cartState is CartState.Error -> {
            Text(
                text = (cartState as CartState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        cartData.isEmpty() -> {
            Text(
                text = "🛒 Giỏ hàng trống",
                modifier = Modifier.padding(24.dp),
                color = Color.Gray
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 20.dp)
            ) {
                items(cartFoods) { food ->
                    val quantity = cartData[food.id] ?: 0
                    Log.i("fixCartScreen", "${food.name} - SL: $quantity")
                    CartItem(
                        food,
                        quantity,
                        onChange = { id, delta -> cartVM.changeQuantity(id, delta) })
                }
            }
        }
    }
}
