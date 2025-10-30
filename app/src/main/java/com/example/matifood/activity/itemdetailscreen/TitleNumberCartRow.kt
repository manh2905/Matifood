package com.example.matifood.activity.itemdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.matifood.R
import com.example.matifood.models.Food
import com.example.matifood.ui.theme.Typography




@Composable
fun TitleNumberRow(
    numberInCart: Int = 1,
    item: Food,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tên món ăn
        Text(
            text = item.name,
            style = Typography.titleLarge,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        // Box chứa + - và số lượng
        ConstraintLayout(
            modifier = Modifier
                .width(120.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            val (plus, minus, number) = createRefs()

            // Dấu trừ
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(colorResource(R.color.orange).copy(alpha = 0.3f), CircleShape)
                    .clickable { onDecrement() }
                    .wrapContentSize(Alignment.Center)
                    .constrainAs(minus) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(
                    text = "−",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Số lượng
            Text(
                text = "$numberInCart",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(number) {
                        start.linkTo(minus.end)
                        end.linkTo(plus.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(horizontal = 12.dp)
            )

            // Dấu cộng
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(colorResource(R.color.orange).copy(alpha = 0.3f), CircleShape)
                    .clickable { onIncrement() }
                    .wrapContentSize(Alignment.Center)
                    .constrainAs(plus) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(
                    text = "+",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
fun prev () {
    val food = Food(
        id = "1",
        name = "Pizza Hải Sản ",
        description = "Pizza phô mai mozzarella, tôm, mực và thanh cua tươi ngon.",
        price = 120000.0,
        category = "Pizza",
        image = "uploads/pizza.png"
    )
    TitleNumberRow(item = food, numberInCart = 1)
}