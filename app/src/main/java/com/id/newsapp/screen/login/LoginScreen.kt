package com.id.newsapp.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.id.newsapp.screen.login.viewmodel.LoginViewModel
import com.id.newsapp.screen.login.viewmodel.event.LoginEvent
import com.id.newsapp.screen.login.viewmodel.state.LoginState
import com.id.newsapp.ui.theme.AppDimens
import com.id.newsapp.ui.theme.attr
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onLoginSuccess = onLoginSuccess,
        onRegisterClick = onRegisterClick
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    // Navigasi hanya saat berhasil login
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onLoginSuccess()
            onEvent(LoginEvent.ResetLoginStatus)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimens.space24),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login", style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(AppDimens.space24))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            label = { Text("Email", style = attr.typography.bodyMedium) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimens.space12))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = { Text("Password", style = attr.typography.bodyMedium) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Edit else Icons.Default.Edit,
                        contentDescription = if (passwordVisible) "Sembunyikan Password" else "Tampilkan Password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimens.space24))

        Button(
            onClick = { onEvent(LoginEvent.Submit) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(AppDimens.space16),
                    strokeWidth = AppDimens.space2,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Masuk")
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(AppDimens.space16))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(AppDimens.space32))

        TextButton(
            onClick = onRegisterClick, modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Belum punya akun? Daftar di sini", style = attr.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MaterialTheme {
        LoginScreen(state = LoginState(), onEvent = {}, onLoginSuccess = {}, onRegisterClick = {})
    }
}