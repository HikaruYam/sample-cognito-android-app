package com.example.awsverificationapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awsverificationapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _signUpState = MutableStateFlow(AuthUiState<Boolean>())
    val signUpState: StateFlow<AuthUiState<Boolean>> = _signUpState

    private val _signInState = MutableStateFlow(AuthUiState<String>())
    val signInState: StateFlow<AuthUiState<String>> = _signInState

    fun signUp(username: String, givenName: String, password: String) {
        _signUpState.value = AuthUiState(isLoading = true) // ロード中の状態を設定
        viewModelScope.launch {
            authRepository.signUp(username, givenName, password) { result ->
                result.onSuccess {
                    _signUpState.value = AuthUiState(data = it) // 成功時の状態を設定
                }.onFailure { error ->
                    _signUpState.value = AuthUiState(error = error.message) // エラー時の状態を設定
                }
            }
        }
    }

    fun signIn(username: String, password: String) {
        _signInState.value = AuthUiState(isLoading = true) // ロード中の状態を設定
        viewModelScope.launch {
            authRepository.signIn(username, password) { result ->
                result.onSuccess {
                    _signInState.value = AuthUiState(data = it) // 成功時の状態を設定
                }.onFailure { error ->
                    _signInState.value = AuthUiState(error = error.message) // エラー時の状態を設定
                }
            }
        }
    }
}

data class AuthUiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)
