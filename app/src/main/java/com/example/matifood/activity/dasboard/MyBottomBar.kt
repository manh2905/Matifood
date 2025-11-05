package com.example.matifood.activity.dasboard

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.matifood.R
import com.example.matifood.ui.theme.Typography
import com.example.matifood.ui.theme.poppinFontFamily
import kotlin.math.log

@Composable
fun MyBottomBar (navController: NavController ) {
    val bottomMenuItemList = prepareBottomMenu()
    val context = LocalContext.current

    val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val token = prefs.getString("token", null)
    var selectedItem by remember { mutableStateOf("Home") }

    BottomAppBar (
        backgroundColor = colorResource(R.color.orange),
        elevation = 8.dp,
        modifier = Modifier
            .height(80.dp)
    ){
        bottomMenuItemList.forEach { bottomBarItem ->
            BottomNavigationItem(
                selected = (selectedItem==bottomBarItem.label),
                onClick = {
                    selectedItem = bottomBarItem.label
                    when (bottomBarItem.label) {
                        "Home" -> navController.navigate("home")
                        "Giỏ hàng" -> navController.navigate("cart")
                        "Đơn hàng" -> navController.navigate("orders")
                        "Tôi" -> {

                                navController.navigate("profile")
                        }
                    }
                }, icon = {
                    Icon(
                        painter = bottomBarItem.iconSelected,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp).size(24.dp)
                    )
                },
                label = {
                    Text(text = bottomBarItem.label ,
                        fontSize = 13.sp, style = Typography.labelSmall,

                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(top = 2.dp)



                    )
                }
            )

        }
    }
}

data class BottomBarItem (
    val label: String, val iconSelected : Painter
)

@Composable
fun prepareBottomMenu() : List<BottomBarItem> {
    val bottomMenuItemList = arrayListOf<BottomBarItem>()
    bottomMenuItemList.add(BottomBarItem(label = "Home", iconSelected = painterResource(R.drawable.homemenu)))
    bottomMenuItemList.add(BottomBarItem(label = "Giỏ hàng", iconSelected = painterResource(R.drawable.cartmenu)))
    bottomMenuItemList.add(BottomBarItem(label = "Đơn hàng", iconSelected = painterResource(R.drawable.ordermenu)))
    bottomMenuItemList.add(BottomBarItem(label = "Tôi", iconSelected = painterResource(R.drawable.usermenu)))

    return bottomMenuItemList

}

//@Preview
//@Composable
//fun SimpleComposablePreview() {
//    MyBottomBar()
//}