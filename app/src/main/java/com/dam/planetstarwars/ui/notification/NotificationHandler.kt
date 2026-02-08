package com.dam.planetstarwars.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.dam.planetstarwars.R
import kotlin.random.Random

class NotificationHandler(private val context: Context) {

    private val notificationManager =
        context.getSystemService(NotificationManager::class.java)

    private val notificationChannelID = "planet_channel_id"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            notificationChannelID,
            "Planetas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de creación de planetas"
        }

        notificationManager.createNotificationChannel(channel)
    }


    fun showSimpleNotification(contentTitle: String, contentText: String) {

        // Construimos la notificación
        val builder = NotificationCompat.Builder(context, notificationChannelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // <--- Asegúrate de tener este icono o pon uno que exista
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)


        val notificationId = Random.nextInt()

        notificationManager.notify(notificationId, builder.build())
    }
}