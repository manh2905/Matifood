package com.example.matifood.activity.dasboard.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.matifood.R
import com.example.matifood.models.Food


@Composable
fun CartItem (item: Food, quantity: Int?, onChange: (String, Int) -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable{},
        horizontalArrangement = Arrangement.SpaceAround

    ){
        AsyncImage(
            model = "http://10.0.2.2:5000/image/${item.image}",
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(9.dp)
                .padding(start = 5.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Column  (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp)

        ) {
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(
                        top = 8.dp
                    ),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(30.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "${item.price.toInt().toString().reversed().chunked(3).joinToString(".").reversed()} USD",
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.SemiBold,

                )

                ConstraintLayout(
                    modifier = Modifier
                        .width(100.dp)
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
                            .size(22.dp)
                            .background(colorResource(R.color.orange).copy(alpha = 0.3f), CircleShape)
                            .clickable {
                                onChange(item.id, -1)
                            }
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // Số lượng
                    Text(
                        text = "${quantity}",
                        color = Color.Black,
                        fontSize = 13.sp,
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
                            .size(22.dp)
                            .background(colorResource(R.color.orange).copy(alpha = 0.3f), CircleShape)
                            .clickable {
                                onChange(item.id, 1)
                            }
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }



        }

    }
}

@Preview
@Composable
fun pre () {
    val food = Food(
        id = "1",
        name = "Pizza Hải Sản ăn là nghiền",
        description = "Pizza phô mai mozzarella, tôm, mực và thanh cua tươi ngon.",
        price = 120000.0,
        category = "Pizza",
        image = "1761620944267food_16.png"
    )
//    CartItem(food, 3)
}