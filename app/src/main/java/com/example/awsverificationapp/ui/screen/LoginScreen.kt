package com.example.awsverificationapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.awsverificationapp.ui.AuthUiState

@Composable
fun LoginScreen(
    signInUiState: AuthUiState<String>,
    onSignInClick: (String, String) -> Unit,
    navigateToSignUpScreen: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Username") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.padding(8.dp),
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Password") },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.padding(8.dp),
        )
        Text(
            "ユーザ作成はこちら",
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navigateToSignUpScreen()
                }
        )
        Button(onClick = { onSignInClick(username.value, password.value) }) {
            Text(text = "Sign In")
        }
        Text(text = "Status")
        if (signInUiState.isLoading) {
            Text(text = "Loading")
        } else if (signInUiState.error != null) {
            Text(text = "Error: ${signInUiState.error}")
        } else if (signInUiState.data != null) {
            Text(text = "Success: ${signInUiState.data}", maxLines = 1)
        }
    }
}