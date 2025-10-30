package com.example.matifood.activity.dasboard

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.matifood.R
import com.example.matifood.activity.itemdetailscreen.DetailFoodScreen
import com.example.matifood.models.Food
import kotlin.random.Random
import kotlin.random.nextInt


@Composable
fun FoodItemCard(item: Food) {
    val randomBought = Random.nextInt(100,200)
    val randomRating = Random.nextDouble(4.0, 5.0)
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .background(colorResource(R.color.white), shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(12.dp)
            .clickable {
                val intent =  Intent(context, DetailFoodScreen::class.java).apply {
                    putExtra("food", item)
                }
                startActivity(context,intent,null)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
//            model = Image(
//                painter = painterResource(R.drawable.food_5),
//                contentDescription = null,
//                modifier = Modifier
//                    .scale(1.1f)
//                    .clip(RoundedCornerShape(12.dp))
//            ),
            model = "http://10.0.2.2:5000/image/${item.image}",
            contentDescription = item.name,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .padding(start = 25.dp)
                .weight(1f)
        ) {
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = String.format("%.1f", randomRating),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )


                Spacer(modifier = Modifier.width(18.dp))

                //  Số lượng đã bán
                Text(
                    text = String.format("%d", randomBought) + " đã bán" ,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Text(
                text = "${item.price.toInt().toString().reversed().chunked(3).joinToString(".").reversed()} USD",
                fontSize = 16.sp,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodItemPreview() {
    val food = Food(
        id = "1",
        name = "Pizza Hải Sản ăn là nghiền",
        description = "Pizza phô mai mozzarella, tôm, mực và thanh cua tươi ngon.",
        price = 120000.0,
        category = "Pizza",
        image = "uploads/pizza.png"
    )
    FoodItemCard(food)
}
