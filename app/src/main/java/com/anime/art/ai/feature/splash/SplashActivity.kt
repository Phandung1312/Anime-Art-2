package com.anime.art.ai.feature.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.anime.art.ai.common.ConfigApp
import com.anime.art.ai.common.extension.startIAP
import com.anime.art.ai.common.extension.startMain
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.databinding.ActivitySplashBinding
import com.anime.art.ai.databinding.DialogNotifyNetworkBinding
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.feature.dialog.NotifyNetworkDialog
import com.basic.common.base.LsActivity
import com.basic.common.extension.getDeviceId
import com.basic.common.extension.isNetworkAvailable
import com.basic.common.extension.makeToast
import com.basic.common.extension.tryOrNull
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : LsActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    @Inject lateinit var prefs: Preferences
    @Inject lateinit var configApp: ConfigApp
    @Inject lateinit var serverApiRepo: ServerApiRepository

    override fun onStart() {
        val isDarkMode = prefs.isDarkMode.get()
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        super.onStart()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initObservable()
        initData()
    }


    private fun initData() {
        syncRemoteConfig {
            Timber.e("id=${getDeviceId()}")
           login()
        }
    }
    private fun login(){
        if(isNetworkAvailable()){
            lifecycleScope.launch(Dispatchers.IO) {
                try{
                    withTimeout(5000){
                        Timber.e("DeviceId = ${getDeviceId()}")
                        serverApiRepo.login(getDeviceId()){ login ->
                            prefs.creditAmount.set(login.credit)
                            prefs.isPremium.set(login.isPremium == 1)
                            lifecycleScope.launch(Dispatchers.Main) {
                                doTask()
                            }
                        }
                    }
                }
                catch (e: TimeoutCancellationException) {
                    lifecycleScope.launch(Dispatchers.Main) {
                        NotifyNetworkDialog({
                            login()
                        }){
                            finish()
                        }.show(supportFragmentManager, null)
                    }
                }
                catch (e : Exception){
                    lifecycleScope.launch(Dispatchers.Main) {
                        NotifyNetworkDialog({
                            login()
                        }){
                            finish()
                        }.show(supportFragmentManager, null)
                    }
                }
            }
        }
        else{
            lifecycleScope.launch(Dispatchers.Main){
               NotifyNetworkDialog({
                    login()
                }){
                    finish()
                }.show(supportFragmentManager, null)
            }
        }
    }
    private fun doTask(){
        when {
            !prefs.isPremium.get() -> startIAP(isFirstScreen = true)
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