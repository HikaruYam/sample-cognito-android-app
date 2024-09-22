package com.example.awsverificationapp.repository

import android.content.Context
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult
import com.example.awsverificationapp.BuildConfig

class CognitoAuthRepositoryImp(context: Context): AuthRepository {
    // Cognitoのユーザプールの設定
    private val userPoolId = BuildConfig.USER_POOL_ID
    private val clientId = BuildConfig.CLIENT_ID
    private val clientSecret = null
    private val region = Regions.AP_NORTHEAST_1
    private val userPool: CognitoUserPool = CognitoUserPool(context, userPoolId, clientId, clientSecret, region)

    override fun signUp(
        username: String,
        givenName: String,
        password: String,
        callback: (Result<Boolean>) -> Unit
    ) {
        val userAttributes = CognitoUserAttributes()
        userAttributes.addAttribute("given_name", givenName)

        userPool.signUpInBackground(username, password, userAttributes, null, object : SignUpHandler {
            override fun onSuccess(user: CognitoUser?, signUpResult: SignUpResult?) {
                if (signUpResult?.isUserConfirmed == true) {
                    callback(Result.success(true)) // 確認済み
                } else {
                    callback(Result.success(false)) // 確認コードの入力が必要
                }
            }

            override fun onFailure(exception: Exception) {
                callback(Result.failure(exception)) // エラーハンドリング
            }
        })
    }

    // サインイン処理の実装
    override fun signIn(
        username: String,
        password: String,
        callback: (Result<String>) -> Unit
    ) {
        val user = userPool.getUser(username)

        user.getSessionInBackground(object : AuthenticationHandler {
            override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice?) {
                val idToken = userSession.idToken.jwtToken
                callback(Result.success(idToken)) // 認証成功でIDトークンを返す
            }

            override fun onFailure(exception: Exception) {
                callback(Result.failure(exception)) // 認証失敗時のエラーハンドリング
            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, userId: String?) {
                val authDetails = AuthenticationDetails(userId, password, null)
                authenticationContinuation.setAuthenticationDetails(authDetails)
                authenticationContinuation.continueTask()
            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                TODO("Not yet implemented")
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation) {
                // 必要に応じて追加の認証チャレンジを処理する
                continuation.continueTask()
            }
        })
    }
}