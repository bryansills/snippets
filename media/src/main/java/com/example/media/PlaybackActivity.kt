package com.example.media

import android.content.ComponentName
import androidx.activity.ComponentActivity
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.MoreExecutors

class PlaybackActivity : ComponentActivity() {
    private lateinit var playerView: PlayerView

    // [START android_media_playback_app_connect_ui]
    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                // Call controllerFuture.get() to retrieve the MediaController.
                // MediaController implements the Player interface, so it can be
                // attached to the PlayerView UI component.
                playerView.setPlayer(controllerFuture.get())
            },
            MoreExecutors.directExecutor()
        )
    }
    // [END android_media_playback_app_connect_ui]
}