package com.id.newsapp.screen.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHostController
import com.id.newsapp.core.components.ScreenWithToolbar
import com.id.newsapp.navigation.Route
import com.id.newsapp.screen.register.viewmodel.RegisterViewModel
import com.id.newsapp.screen.register.viewmodel.event.RegisterEvent
import com.id.newsapp.screen.register.viewmodel.state.RegisterState
import com.id.newsapp.ui.theme.AppDimens
import com.id.newsapp.ui.theme.attr
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterRoute(
    navController: NavHostController, viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) {
            navController.navigate(Route.LOGIN) {
                popUpTo(Route.REGISTER) { inclusive = true }
            }
            viewModel.onEvent(RegisterEvent.Reset)
        }
    }

    ScreenWithToolbar(modifier = Modifier.fillMaxSize(),
        showBackButton = true,
        onBackClick = {
            navController.popBackStack()
        }, toolbarContent = {
            Text("Register", style = attr.typography.headlineLarge)
        },
        body = {
            RegisterScreen(
                state = state, onEvent = viewModel::onEvent
            )
        }
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState, onEvent: (RegisterEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppDimens.space24),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Buat Akun", style = attr.typography.headlineMedium)

        Spacer(modifier = Modifier.height(AppDimens.space16))

        OutlinedTextField(
            value = state.email,
            onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimens.space12))

        OutlinedTextField(
            value = state.password,
            onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimens.space24))

        Button(
            onClick = { onEvent(RegisterEvent.Submit) },
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
                Text("Daftar")
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(AppDimens.space16))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = attr.typography.bodyMedium
            )
        }
    }
}