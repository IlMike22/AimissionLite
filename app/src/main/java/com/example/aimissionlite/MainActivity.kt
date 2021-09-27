package com.example.aimissionlite

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // setup notifications
        createNotificationChannel()

        // create the intent and pending intent
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        // setup the notification builder

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_info)
            .setContentTitle("AimissionLite")
            .setContentText("Dont forget to finish your goals!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Hey dont forget to finish your goals")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // show the notification
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, notificationBuilder.build())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun hideKeyboard(currentFocusedView: View?) {
        try {
            val inputMethodService =
                this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodService.hideSoftInputFromWindow(currentFocusedView?.windowToken, 0)
        } catch (error: Throwable) {
            Log.e("AimissionLite", "Unable to close keyboard. Details: ${error.message}")
        }
    }

    // create the notification channel

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.channel_name)
            val channelDescription = getString(R.string.channel_description)
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, channelImportance).apply {
                    description = channelDescription
                }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }



    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "AIMISSION_CHANNEL_ID"
    }
}