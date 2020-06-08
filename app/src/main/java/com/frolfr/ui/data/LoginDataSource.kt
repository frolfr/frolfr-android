package com.frolfr.ui.data

import android.content.Context
import android.util.Log
import com.frolfr.api.FrolfrApi
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.api.model.LoginRequest
import com.frolfr.config.PreferenceKeys
import com.frolfr.ui.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        PreferenceKeys.AuthKeys::class.java.name, Context.MODE_PRIVATE
    )

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        val loginRequest = LoginRequest(email, password)

        return try {
            val loginResponse = FrolfrApi.retrofitService.login(loginRequest)

            sharedPreferences
                .edit()
                .putString(PreferenceKeys.AuthKeys.TOKEN.toString(), loginResponse.token)
                .putString(PreferenceKeys.AuthKeys.EMAIL.toString(), email)
                .commit()

            FrolfrAuthorization.authToken = loginResponse.token
            FrolfrAuthorization.email = email

            val loggedInUser =
                LoggedInUser(
                    email,
                    loginResponse.token
                )
            Result.Success(loggedInUser)

        } catch (t: Throwable) {
            Log.i("frolfrLogin", "Got error result")
            Result.Error(t as Exception)
        }
    }

    fun logout() {
        sharedPreferences
            .edit()
            .remove(PreferenceKeys.AuthKeys.TOKEN.toString())
            .commit()
    }
}

