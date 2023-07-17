package com.anime.art.ai.domain.manager

interface NotificationManager {

    fun cancelNotify(id: Int)

    fun createNotificationChannel(id: Int = 0)

    fun buildNotificationChannelId(id: Int): String

    fun notify(title: String, body: String)

}