package com.anime.art.ai.common

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.anime.art.ai.data.Preferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.HiltAndroidApp
import glimpse.core.Glimpse
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject lateinit var prefs : Preferences
    companion object {
        lateinit var app: App
    }

    init {
        app = this
    }

    override fun onCreate() {
        super.onCreate()
        val isDarkMode = prefs.isDarkMode.get()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        // Setup Timber
        Timber.plant(Timber.DebugTree())

        // RxThrowable
        RxJavaPlugins.setErrorHandler { e ->
            Timber.e("Error: $e")
        }

        // Register firebase token
        Firebase.messaging.isAutoInitEnabled = true
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w( "Fetching FCM registration token failed")
                return@OnCompleteListener
            }
            Timber.d("Token FCM: " + task.result)
        })
        Glimpse.init(this)
    }

}