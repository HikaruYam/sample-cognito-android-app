package com.example.awsverificationapp.di

import com.example.awsverificationapp.repository.AuthRepository
import com.example.awsverificationapp.repository.CognitoAuthRepositoryImp
import com.example.awsverificationapp.ui.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { CognitoAuthRepositoryImp(context = get()) }
    viewModel { AuthViewModel(authRepository = get()) }
}
