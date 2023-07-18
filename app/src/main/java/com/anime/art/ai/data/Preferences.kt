package com.anime.art.ai.data

import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    private val rxPrefs: RxSharedPreferences
) {

    companion object {

    }

    // Config
    val isUpgraded = rxPrefs.getBoolean("isUpgraded", false)
    val timeExpiredPremium = rxPrefs.getLong("timeExpiredPremium", -1) // Milliseconds
    val versionGallery = rxPrefs.getLong("versionGallery", 0)


}
