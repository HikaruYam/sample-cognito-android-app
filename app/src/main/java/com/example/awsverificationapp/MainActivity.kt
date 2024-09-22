package com.example.awsverificationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.awsverificationapp.di.appModule
import com.example.awsverificationapp.ui.AppNavigation
import com.example.awsverificationapp.ui.theme.AWSVerificationAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin{
            androidContext(this@MainActivity)
            modules(appModule)
        }
        enableEdgeToEdge()
        setContent {
            AWSVerificationAppTheme {
                AppNavigation()
            }
        }
    }
}

