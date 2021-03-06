package com.kiprosh.optimizeprime.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.view.activity.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val defaultChannel = "Default Channel"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, defaultChannel)
            .setSmallIcon(R.drawable.ic_stat_celebration)
            .setContentTitle(message.notification!!.title)
            .setContentText(message.notification!!.body).setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (message.notification!!.imageUrl != null) {
            val bigPicture = NotificationCompat.BigPictureStyle()
            val futureTarget = Glide.with(this)
                .asBitmap()
                .load(message.notification!!.imageUrl)
                .submit()
            val bitmap = futureTarget.get()
            builder.setStyle(bigPicture.bigPicture(bitmap))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                defaultChannel,
                defaultChannel,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, builder.build())
    }
}
