package com.anime.art.ai.data

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    private val rxPrefs: RxSharedPreferences
) {
    private val creditAmountSubject: PublishSubject<Long> = PublishSubject.create()
    companion object {

    }

    val isFirstLogin = rxPrefs.getBoolean("isFirstLogin", true)
    val isUpgraded = rxPrefs.getBoolean("isUpgraded", false)
    val timeExpiredPremium = rxPrefs.getLong("timeExpiredPremium", -1)
    val versionGallery = rxPrefs.getLong("versionGallery", 0)
    val isPremium = rxPrefs.getBoolean("isPremium", false)
    val creditAmount = rxPrefs.getLong("creditAmount", 0)

    val consecutiveSeries = rxPrefs.getInteger("consecutiveSeries", 0)
    val isReceived = rxPrefs.getBoolean("isReceived", false)
    val isSynced = rxPrefs.getBoolean("isSynced", false)
    val isFirstInputImage = rxPrefs.getBoolean("isFirstInputImage", true)

    val isDarkMode = rxPrefs.getBoolean("isDarkMode", false)
}
