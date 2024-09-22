package com.example.awsverificationapp.repository

interface AuthRepository {
    fun signUp(username: String, givenName: String, password: String, callback: (Result<Boolean>) -> Unit)
    fun signIn(username: String, password: String, callback: (Result<String>) -> Unit)
}