package com.example.media

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

// [START android_media_playback_app_implement_service]
class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    // Create your Player and MediaSession in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    // @@@ Bryan start omit this in android_media_playback_app_implement_service
    // [START android_media_playback_app_service_session]
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession
    // [START android_media_playback_app_service_session]
    // @@@ Bryan end omit this in android_media_playback_app_implement_service

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
// [END android_media_playback_app_implement_service]