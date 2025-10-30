package com.example.matifood.activity.itemdetailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.matifood.models.Food

@Preview
@Composable
fun pre () {
//    val food = Food(
//        id = "1",
//        name = "Pizza Hải Sản ăn là nghiền",
//        description = "Pizza phô mai mozzarella, tôm, mực và thanh cua tươi ngon.",
//        price = 120000.0,
//        category = "Pizza",
//        image = "uploads/pizza.png"
//    )
//    HeaderDetailScreen(food)
}
@Composable
fun HeaderDetailScreen (
    item : Food,
    onClickBack: () -> Unit = {
    }
) {
    val curvedShape = GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val radius = width / 9  // độ cong nửa dưới, có thể điều chỉnh

        moveTo(0f, 0f)
        lineTo(0f, height - radius)
        quadraticBezierTo(width / 2, height + radius, width, height - radius)
        lineTo(width, 0f)
        close()
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp)
    ) {
        val (back, image, arc) = createRefs()
        AsyncImage(
            model = "http://10.0.2.2:5000/image/${item.image}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(curvedShape)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        )
        BackButton(
            onClick = onClickBack,
            modifier = Modifier
                .constrainAs(back){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
    }
}

@Composable
fun  BackButton (
    onClick: ()-> Unit,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(start = 20.dp, top = 90.dp)
            .background(Color.White, shape = CircleShape)
            .size(40.dp)

    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.Black
        )
    }
}