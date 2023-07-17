package com.anime.art.ai.common

import android.content.Context
import android.net.Uri
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.anime.art.ai.data.Preferences
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigApp @Inject constructor(
    prefs: Preferences
) {

    var versionGallery = prefs.versionGallery.get()


}