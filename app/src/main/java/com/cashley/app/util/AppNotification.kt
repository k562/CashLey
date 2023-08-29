package com.cashley.app.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.cashley.app.R
import com.cashley.app.activity.OfferDetailActivity
import com.cashley.app.activity.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Date

class AppNotification : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showNotification(message)
    }

    private fun showNotification(message: RemoteMessage) {
        if (message != null) {
            val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
            val title: String? = message.notification?.title
            val msg: String? = message.notification?.body
            Log.d("TAG1111", "sendMessage: $msg title: $title")
            val CHANNEL_ID = applicationContext.getString(R.string.app_name)

            // Data key
            val OFFER_ID_KEY = "offerId"

            val offerId: String? = message.data[OFFER_ID_KEY]
            val intent: Intent

            if (offerId != null && offerId.isNotBlank()) {
                //val bundle = Bundle()
                intent = Intent(applicationContext, OfferDetailActivity::class.java)
                intent.putExtra("offerId", offerId)
                //intent.putExtras(bundle)
            } else {
                intent = Intent(applicationContext, SplashActivity::class.java)
            }

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                121,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.addLine(msg)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = getString(R.string.app_name)
                val description = "Notification"
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = description
                channel.enableVibration(true)
                channel.lightColor = Color.BLUE
                channel.setShowBadge(true)
                notificationManager.createNotificationChannel(channel)
            }

            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setVibrate(longArrayOf(0, 100))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setLights(Color.BLUE, 3000, 3000)
                    .setAutoCancel(false)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_money_icon)
                    .setColor(ContextCompat.getColor(applicationContext, R.color.white))
                    .setStyle(inboxStyle)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            applicationContext.resources,
                            R.mipmap.ic_launcher
                        )
                    )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder.setChannelId(CHANNEL_ID)
            }

            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            notificationManager.notify(m, notificationBuilder.build())
        }
    }

}