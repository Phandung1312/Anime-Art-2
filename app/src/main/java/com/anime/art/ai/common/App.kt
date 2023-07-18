package com.anime.art.ai.common

import android.app.Application
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var app: App
    }

    init {
        app = this
    }

    override fun onCreate() {
        super.onCreate()

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
    }

}