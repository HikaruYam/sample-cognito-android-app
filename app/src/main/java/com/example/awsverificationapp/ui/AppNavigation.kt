package com.example.awsverificationapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.example.awsverificationapp.ui.screen.LoginScreen
import com.example.awsverificationapp.ui.screen.SignUpScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = koinViewModel()
){
    val navController = rememberNavController()
    val signInState by authViewModel.signInState.collectAsState()
    val signUpState by authViewModel.signUpState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Login App")
                },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.LOGIN.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.LOGIN.name) {
                LoginScreen(
                    signInUiState = signInState,
                    onSignInClick = { username, password ->
                        authViewModel.signIn(username, password)
                    },
                    navigateToSignUpScreen = {
                        navController.navigate(Route.SIGN_UP.name)
                    }
                )
            }
            composable(Route.SIGN_UP.name) {
                SignUpScreen(
                    signUpState = signUpState,
                    onSignUpClick = { username, givenName, password ->
                        authViewModel.signUp(username, givenName, password)
                    },
                    navigateToLoginScreen = {
                        navController.navigate(Route.LOGIN.name)
                    }
                )
            }
        }
    }
}

private enum class Route {
    LOGIN,
    SIGN_UP,

}
