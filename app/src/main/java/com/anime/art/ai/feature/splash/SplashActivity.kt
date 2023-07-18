package com.anime.art.ai.feature.splash

import android.annotation.SuppressLint
import android.os.Bundle
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.startMain
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.ActivitySplashBinding
import com.basic.common.base.LsActivity
import com.basic.common.extension.tryOrNull
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : LsActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    @Inject lateinit var prefs: Preferences
    @Inject lateinit var configApp: ConfigApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
    }


    private fun initData() {
        syncRemoteConfig { doTask() }
    }

    private fun doTask(){
        when {
            !prefs.isUpgraded.get() -> startMain()
            else -> startMain()
        }
        finish()
    }

    private fun syncRemoteConfig(done: () -> Unit) {
        Firebase.remoteConfig.let { config ->
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
            config.setConfigSettingsAsync(configSettings)
            config
                .fetchAndActivate()
                .addOnSuccessListener {
                    configApp.versionGallery = tryOrNull { config.getLong("versionGallery") } ?: configApp.versionGallery

                    Timber.e("##### REMOTE CONFIG #####")
                    Timber.e("versionGallery: ${configApp.versionGallery}")
                    Timber.e("##### REMOTE CONFIG #####")

                    done()
                }
                .addOnFailureListener {
                    done()
                }
        }
    }

    private fun initObservable() {

    }

    private fun initView() {

    }

}