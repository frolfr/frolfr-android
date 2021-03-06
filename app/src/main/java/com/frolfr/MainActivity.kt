package com.frolfr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.frolfr.api.FrolfrAuthorization
import com.frolfr.config.PreferenceKeys
import com.frolfr.db.FrolfrDatabase
import com.frolfr.ui.UserViewModel
import com.frolfr.ui.login.data.LoginDataSource
import com.frolfr.ui.login.LoginActivity
import com.frolfr.ui.login.data.LoginRepository
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val userViewModel = UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FrolfrDatabase.init(applicationContext)

        val preferences = getSharedPreferences(
            PreferenceKeys.AuthKeys::class.java.name,
            Context.MODE_PRIVATE
        )
        val existingAuthToken =
            preferences.getString(PreferenceKeys.AuthKeys.TOKEN.toString(), null)
        val authTokenEnv = preferences.getString(PreferenceKeys.AuthKeys.ENV.toString(), null)
        if (existingAuthToken == null || authTokenEnv == null || authTokenEnv != BuildConfig.BUILD_TYPE) {
            navigateToLoginActivity()
            return
        } else {
            val existingEmail =
                preferences.getString(PreferenceKeys.AuthKeys.EMAIL.toString(), null)
            val existingUserId =
                preferences.getInt(PreferenceKeys.AuthKeys.USER_ID.toString(), -1)
            FrolfrAuthorization.authToken = existingAuthToken
            FrolfrAuthorization.email = existingEmail
            FrolfrAuthorization.userId = existingUserId
        }

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_rounds, R.id.nav_courses, R.id.nav_friends, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // TODO better way using ViewModel?
        val headerLayout: View = navView.getHeaderView(0)
        val userImageView = headerLayout.findViewById<ImageView>(R.id.navHeaderUserImage)
        val userNameTextView = headerLayout.findViewById<TextView>(R.id.navHeaderUserName)

        userViewModel.currentUser.observe(this, Observer { user ->
            Glide.with(this).load(user?.avatarUri?.toUri())
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                ).into(userImageView)
            userNameTextView.text = "${user?.nameFirst} ${user?.nameLast}".trim()
        })

        try {
            userViewModel.setupCurrentUser()
        } catch (t: Throwable) {
            logout()
        }
    }

    private fun logout() {
        LoginRepository(
            dataSource = LoginDataSource(applicationContext)
        ).logout()
        navigateToLoginActivity()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
