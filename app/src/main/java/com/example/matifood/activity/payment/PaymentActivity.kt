package com.example.matifood.activity.payment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.matifood.models.OrderItem
import com.example.matifood.viewmodel.PaymentViewModel
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matifood.viewmodel.CartViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class PaymentActivity : ComponentActivity() {

    private val paymentVM = PaymentViewModel()

    private lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51SIuTDAycn184MqWcc7Mhy7KzGUJ2lWDcjdbUW6c0YNiPhgspVXN7E57qC3dEL2lMMVWPbvwk9CmSw4RcqDtb1Ie00EjCVYQZ5"
        )

        // ⚡ Bước 2: Tạo PaymentSheet trước setContent()
        paymentSheet = PaymentSheet(this) { result ->
            when (result) {
                is PaymentSheetResult.Completed -> {

                    // 🔹 Gọi verifyOrder

                    paymentVM.verifyOrder(paymentVM.currentOrderId, true) { success ->
                        if (success){
                            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else Toast.makeText(this, "Không xác nhận được đơn hàng!", Toast.LENGTH_SHORT).show()
                    }
                }

                is PaymentSheetResult.Canceled -> {
                    Toast.makeText(this, " Đã hủy thanh toán", Toast.LENGTH_SHORT).show()
                    paymentVM.verifyOrder(paymentVM.currentOrderId, false) {}
                }

                is PaymentSheetResult.Failed ->
                    Toast.makeText(this, " Lỗi: ${result.error.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }

        val orderSummary = intent.getSerializableExtra("items") as? ArrayList<OrderItem> ?: arrayListOf()
        val totalPrice = intent.getDoubleExtra("total_price", 0.0)

        setContent {
            PaymentScreen(
                orderSummary = orderSummary,
                totalPrice = totalPrice,
                onBack = { finish() },
                paymentVM = paymentVM,
                onPay = { clientSecret ->

                    lifecycleScope.launch {
                        try {
                            Log.i("StripeDebug", "🔑 ClientSecret: $clientSecret")
                            paymentSheet.presentWithPaymentIntent(
                                clientSecret,
                                PaymentSheet.Configuration(
                                    merchantDisplayName = "MATIFOOD",
                                    allowsDelayedPaymentMethods = true
                                )
                            )
                        } catch (e: Exception) {
                            Toast.makeText(this@PaymentActivity, "Lỗi hiển thị Stripe: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.e("StripeDebug", "❌ Stripe PaymentSheet lỗi", e)
                        }
                    }
                }
            )
        }
    }



}

@Composable
fun PaymentScreen(
    orderSummary: List<OrderItem> = emptyList(),
    totalPrice: Double = 0.0,
    onBack: () -> Unit = {},
    paymentVM: PaymentViewModel = viewModel(),
    onPay: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Thanh toán", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                backgroundColor = Color(0xFFFF9800),
                modifier = Modifier
                    .padding(top = 0.dp)
                    .height(70.dp)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {

            Column(
                modifier = Modifier
                    .weight(1f) //  đẩy footer xuống đáy
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Text("Danh sách món", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        orderSummary.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.name} x${item.quantity}", fontSize = 16.sp, modifier = Modifier.weight(1f))

                            }
                            Divider(color = Color(0xFFEEEEEE))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Phí vận chuyển: 2 USD", fontSize = 18.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))

                // Tổng tiền vẫn giữ trong phần scroll (hoặc bạn có thể chuyển xuống footer nếu muốn)
                Text(
                    text = "Tổng cộng: ${"%.2f".format(totalPrice + 2)} USD",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(thickness = 1.dp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(24.dp))

                Text("Thông tin vận chuyển", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Họ và tên") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address, onValueChange = { address = it },
                    label = { Text("Địa chỉ") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = city, onValueChange = { city = it },
                    label = { Text("Thành phố") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )

                // Thêm spacer để nội dung cuối không bị sát quá khi cuộn hết
                Spacer(modifier = Modifier.height(20.dp))
            }


            Surface(
                elevation = 16.dp, // Tạo bóng đổ để tách biệt với nội dung cuộn
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (name.isNotBlank() && phone.isNotBlank() && address.isNotBlank() && city.isNotBlank()) {
                                Toast.makeText(context, "Đang khởi tạo thanh toán...", Toast.LENGTH_SHORT).show()
                                paymentVM.createPaymentIntent(
                                    amount = totalPrice + 2,
                                    items = orderSummary,
                                    address = "$name, $phone, $address, $city"
                                ) { clientSecret ->
                                    if (!clientSecret.isNullOrEmpty()) onPay(clientSecret)
                                    else Toast.makeText(context, "Lỗi tạo thanh toán", Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(context, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF9800)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("THANH TOÁN NGAY", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}




