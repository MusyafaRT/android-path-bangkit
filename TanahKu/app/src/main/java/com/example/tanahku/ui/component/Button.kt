package com.example.tanahku.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TanahKuButton(title: String, onClick: () -> Unit,  modifier: Modifier = Modifier){
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff26623e)),
        contentPadding = PaddingValues(horizontal = 45.dp, vertical = 13.dp),
        border = BorderStroke(1.dp, Color(0xff6a2e35)),
        modifier = modifier
            .requiredWidth(width = 113.dp)
            .requiredHeight(height = 31.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .requiredWidth(width = 113.dp)
                .requiredHeight(height = 31.dp)
        ) {
            Text(
                text = title,
                color = Color(0xfffbf3e0),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
    }
}