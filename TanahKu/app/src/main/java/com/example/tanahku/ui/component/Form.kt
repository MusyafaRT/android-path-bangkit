package com.example.tanahku.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tanahku.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(
    titleId: Int,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val emailRegex = Regex("^([\\w-]+(?:\\.[\\w-]+)*)@([a-zA-Z]+(?:\\.[a-zA-Z]+)+)\$")
    val minLength = 8
    val title = stringResource(id = titleId)
    Column(
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = title,
            color = Color(0xff2e2836),
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                onValueChanged(it)
                if (titleId == R.string.email) {
                    isError = !emailRegex.matches(it)
                }

                if (titleId == R.string.password) {
                    isError = it.length < minLength
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            visualTransformation = if (title == stringResource(id = R.string.password)) PasswordVisualTransformation() else VisualTransformation.None ,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xfffffaef)
            ),
            supportingText = {
                if (isError) {
                    val errorMessage = when (titleId) {
                        R.string.email -> {
                            "Invalid email format"
                        }
                        R.string.password -> {
                            "Password must be at least $minLength characters"
                        }
                        else -> {
                            ""
                        }
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (isError)
                    Icon(Icons.Filled.Info,"error", tint = MaterialTheme.colorScheme.error)
            },
            shape = RoundedCornerShape(8.dp),
            modifier = modifier.height(50.dp)
        )
    }
}
