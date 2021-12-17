package com.example.myplayer

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.myplayer.Constants.getDefaultAlbumArt

class NotificationService : Service() {

    var status: Notification? = null
    lateinit var views:RemoteViews
    lateinit var bigViews:RemoteViews

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == Constants.ACTION.STARTFOREGROUND_ACTION) {
            showNotification()
        } else if (intent.action == Constants.ACTION.BACKWARD_ACTION) {
            AudioActivity.mp?.getCurrentPosition()?.minus(10000)?.let { AudioActivity.mp?.seekTo(it) }
        } else if (intent.action == Constants.ACTION.PLAY_ACTION) {
            if (AudioActivity.mp?.isPlaying == true) {
                // Stop
                AudioActivity.mp?.pause()
            } else {
                // Start
                AudioActivity.mp?.start()
            }
        } else if (intent.action == Constants.ACTION.FORWARD_ACTION) {
            AudioActivity.mp?.getCurrentPosition()?.plus(10000)?.let { AudioActivity.mp?.seekTo(it) }
        } else if (intent.action == Constants.ACTION.STOPFOREGROUND_ACTION) {
            stopForeground(true)
            stopSelf()
        }
        return START_STICKY
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification()
    {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        views = RemoteViews(packageName, R.layout.status_bar)
        bigViews = RemoteViews(packageName, R.layout.status_bar_expanded)

        // showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE)
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE)
        bigViews.setImageViewBitmap(
            R.id.status_bar_album_art,
            getDefaultAlbumArt(this)
        )
        val notificationIntent = Intent(this, AudioActivity::class.java)
        notificationIntent.action = Constants.ACTION.MAIN_ACTION
        notificationIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val previousIntent = Intent(this, NotificationService::class.java)
        previousIntent.action = Constants.ACTION.BACKWARD_ACTION
        val ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0)
        val playIntent = Intent(this, NotificationService::class.java)
        playIntent.action = Constants.ACTION.PLAY_ACTION
        val pplayIntent = PendingIntent.getService(this, 0, playIntent, 0)
        val nextIntent = Intent(this, NotificationService::class.java)
        nextIntent.action = Constants.ACTION.FORWARD_ACTION
        val pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0)
        val closeIntent = Intent(this, NotificationService::class.java)
        closeIntent.action = Constants.ACTION.STOPFOREGROUND_ACTION
        val pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0)

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent)
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent)
        views.setOnClickPendingIntent(R.id.status_bar_forward, pnextIntent)
        bigViews.setOnClickPendingIntent(R.id.status_bar_forward, pnextIntent)
        views.setOnClickPendingIntent(R.id.status_bar_backward, ppreviousIntent)
        bigViews.setOnClickPendingIntent(R.id.status_bar_backward, ppreviousIntent)
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent)

        views.setTextViewText(R.id.status_bar_track_name, myListSong[currentSongPosition].Title)
        bigViews.setTextViewText(R.id.status_bar_track_name, myListSong[currentSongPosition].Title)
        views.setTextViewText(R.id.status_bar_artist_name, myListSong[currentSongPosition].AuthorName)
        bigViews.setTextViewText(R.id.status_bar_artist_name, myListSong[currentSongPosition].AuthorName)

        status = Notification.Builder(this, channelId).build()
        status!!.contentView = views
        status!!.bigContentView = bigViews
        status!!.flags = Notification.FLAG_ONGOING_EVENT
        status!!.icon = R.drawable.ic_launcher
        status!!.contentIntent = pendingIntent
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}