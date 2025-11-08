package com.example.matifood.activity.dasboard.cart

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.matifood.R
import com.example.matifood.activity.payment.PaymentActivity
import com.example.matifood.models.Food
import com.example.matifood.models.OrderItem
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
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                    cartVM.fetchCart()

            }
        }
        // Thêm observer
        lifecycleOwner.lifecycle.addObserver(observer)

        // Xóa observer khi composable bị hủy
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val isLogin by authVM.isLoggedIn.collectAsState()
    val isEmty by cartVM.isEmty.collectAsState()
    val cartState by cartVM.cartState.collectAsState()
    val cartData by cartVM.cartData.collectAsState()
    val context = LocalContext.current

    var cartFoods by remember { mutableStateOf<List<Food>>(emptyList()) }
    var isLoadingFoods by remember { mutableStateOf(false) }

    LaunchedEffect(isLogin) {
        if (isLogin) cartVM.fetchCart()
    }
//    cartData
    LaunchedEffect( !isEmty) {
        if (cartData.isNotEmpty()) {
            val ids = cartData.keys.toList()
            isLoadingFoods = true
            foodVM.fetchFoodsByIds(ids) { foods ->
                if (foods != null) cartFoods = foods else Log.e("CartScreen", " Không tải được danh sách món ăn")
                isLoadingFoods = false
            }
        } else {
            cartFoods = emptyList()
        }
    }


    val totalPrice = cartFoods.sumOf { food -> (food.price * (cartData[food.id] ?: 0)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        when {
            !isLogin -> {
                Text(
                    text = "Vui lòng đăng nhập để xem giỏ hàng.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }

//            cartState is CartState.Loading || isLoadingFoods -> {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            }

            cartState is CartState.Error -> {
                Text(
                    text = (cartState as CartState.Error).message,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            cartData.isEmpty() -> {
                Text(
                    text = "Giỏ hàng trống",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        text = "Giỏ hàng của bạn ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                            .padding(top = 25.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .weight(1f)
                    ) {
                        items(cartFoods) { food ->
                            val quantity = cartData[food.id] ?: 0
                            CartItem(
                                item = food,
                                quantity = quantity,
                                onChange = { id, delta ->
                                    cartVM.changeQuantity(id, delta)
                                }
                            )
                        }
                    }
                }

                // 🔹 Footer thanh toán
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .shadow(10.dp)
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Tổng cộng:",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "${totalPrice.toInt().toString().reversed().chunked(3).joinToString(".").reversed()} USD",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                        }

                        Button(
                            onClick = {
                                val intent = Intent(context, PaymentActivity::class.java).apply {

                                    val orderSummary : List<OrderItem> = cartFoods.map { food ->
                                        val quantity = cartData[food.id] ?: 0

                                        OrderItem(
                                            name = food.name,
                                            quantity = quantity
                                        )

                                    }

                                    putExtra("items", ArrayList(orderSummary))
                                    putExtra("total_price", totalPrice)

                                }
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.orange)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .width(150.dp)
                        ) {
                            Text(
                                text = "Thanh toán",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
