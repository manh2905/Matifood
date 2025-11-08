package com.example.matifood.activity.dasboard.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matifood.viewmodel.OrderState
import com.example.matifood.viewmodel.OrderViewModel
import com.example.matifood.models.OrderResponse
import com.example.matifood.viewmodel.AuthViewModel

@Composable
fun OrderScreen(
    authVM: AuthViewModel ,
    orderVM: OrderViewModel
) {
    val isLogin by authVM.isLoggedIn.collectAsState()
    val orders by orderVM.orders.collectAsState()
    val orderState by orderVM.orderState.collectAsState()

    LaunchedEffect(isLogin) {
        if (isLogin) orderVM.fetchOrders()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Đơn hàng của tôi",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(top = 20.dp)

                    )
                },
                backgroundColor = Color(0xFFFF9800),
                modifier = Modifier
                    .height(70.dp)
//                actions = {
//                    IconButton(onClick = { if (isLogin) orderVM.fetchOrders() }) {
//                        Icon(Icons.Default.Refresh, contentDescription = "Làm mới", tint = Color.White)
//                    }
//                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {
            when {
                !isLogin -> {
                    Text(
                        text = "Vui lòng đăng nhập để xem đơn hàng.",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                orderState is OrderState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFFF9800)
                    )
                }

                orderState is OrderState.Error -> {
                    Text(
                        text = (orderState as OrderState.Error).message,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                orders.isEmpty() -> {
                    Text(
                        text = "Đơn hàng của bạn đang trống ",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        items(orders) { order ->
                            OrderCard(order)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderResponse) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 6.dp,
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Mã đơn: ${order._id.takeLast(6).uppercase()}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Tổng tiền: ${order.amount} USD", color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Địa chỉ: ${order.address}", color = Color.DarkGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (order.payment) "Đã thanh toán " else "Chưa thanh toán ❌",
                color = if (order.payment) Color(0xFF4CAF50) else Color.Red,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Trạng thái: ${order.status.ifBlank { "Đang xử lý" }}",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}
