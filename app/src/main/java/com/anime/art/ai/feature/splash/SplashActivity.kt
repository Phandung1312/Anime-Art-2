package com.anime.art.ai.feature.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.startMain
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.ActivitySplashBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.basic.common.base.LsActivity
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.tryOrNull
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.uber.autodispose.android.autoDispose
import com.uber.autodispose.android.lifecycle.autoDispose
import com.uber.autodispose.android.lifecycle.scope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : LsActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    @Inject lateinit var prefs: Preferences
    @Inject lateinit var configApp: ConfigApp
    @Inject lateinit var serverApiRepo: ServerApiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
    }


    private fun initData() {
        syncRemoteConfig {
            lifecycleScope.launch(Dispatchers.IO) {
                serverApiRepo.login(getDeviceId()){login ->
                    prefs.creditAmount.set(login.credit)
                    lifecycleScope.launch(Dispatchers.Main) {
                        doTask()
                    }
                }
            }
        }
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