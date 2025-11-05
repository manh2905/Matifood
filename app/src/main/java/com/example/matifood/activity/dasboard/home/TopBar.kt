package com.example.matifood.activity.dasboard.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matifood.R
import com.example.matifood.ui.theme.Typography

@Composable
@Preview
fun TopBar () {
    Row(modifier = Modifier
        .padding(top = 20.dp)
        .padding(horizontal = 12.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(

            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,

            modifier = Modifier
//                .clickable{}
                .size(80.dp)
                .clip(RoundedCornerShape(30.dp))
        )

        var text by rememberSaveable { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = {it ->
                text = it
            },
            placeholder = {
                Text(
                    text = "Bạn muốn ăn gì ?",
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    style = Typography.bodySmall
                )
            },
            trailingIcon = {
                IconButton(onClick = {


                }) {
                    Icon(
                        painter = painterResource(R.drawable.searchicon),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = Color.Gray
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = colorResource(R.color.white),
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                textColor = Color.Gray,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
                .height(60.dp)
                .background(Color.Gray, CircleShape)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(25.dp),
                    clip = true
                )
        )



    }
}