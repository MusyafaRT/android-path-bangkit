package com.example.tanahku.ui.auth.register

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tanahku.ui.component.Form
import com.example.tanahku.R
import com.example.tanahku.ViewModelFactory
import com.example.tanahku.di.Injection
import com.example.tanahku.ui.auth.login.Footer
import com.example.tanahku.ui.auth.login.Header
import com.example.tanahku.ui.auth.login.LogoAndTitle
import com.example.tanahku.ui.component.TanahKuButton

@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        LogoAndTitle()
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Form(titleId =  R.string.email, onValueChanged = {value ->
            email = value
        })
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Form(titleId =  R.string.username, onValueChanged = { value ->
            username = value
        })
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Form(titleId =  R.string.password, onValueChanged = { value ->
            password = value
        })
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        RegisterButtonAndSignInText(navHostController, onClick = {
            viewModel.postUser(username, email, password)
            Log.d("Register", username + email + password)
        })
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Footer()
    }
}

@Composable
fun RegisterButtonAndSignInText(navHostController: NavHostController, onClick: () -> Unit ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TanahKuButton(title = "Register", onClick = onClick)
        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xff2e2836),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Sudah punya akun? Masuk")
                }
            },
            onClick = {
                navHostController.navigate("login")
            }
        )
    }
}

