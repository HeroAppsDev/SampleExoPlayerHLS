package com.haroldcalayan.sampleexoplayer.presentation.hls

import android.app.Application
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsManifest
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.haroldcalayan.sampleexoplayer.data.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val _exoPlayer: ExoPlayer,
    private val mediaRepository: MediaRepository,
    application: Application
) : AndroidViewModel(application) {

    private var isPlayerReleased = false

    val exoPlayer: ExoPlayer
        get() = _exoPlayer

    init {
        // Initialize the player
        fetchMedia()
        initializePlayer()
    }

    private fun initializePlayer() {
        _exoPlayer.addListener(object : Player.Listener {

        })

        _exoPlayer.addListener(
            object : Player.Listener {
                @OptIn(UnstableApi::class)
                override fun onTimelineChanged(timeline: Timeline, @Player.TimelineChangeReason reason: Int) {
                    val manifest = _exoPlayer.currentManifest
                    if (manifest is HlsManifest) {
                        // Do something with the manifest.
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("PlayerViewModel", "Playback error: ${error.message}", error)
                }
            }
        )
    }

    @OptIn(UnstableApi::class)
    fun fetchMedia() {
        if (!isPlayerReleased) {
            viewModelScope.launch {
                try {
                    val uri = mediaRepository.getMediaUri()
                    val mediaItem = MediaItem.fromUri(uri)

                    val hlsMediaSource = HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                        .setAllowChunklessPreparation(false)
                        .createMediaSource(mediaItem)

                    _exoPlayer.setMediaSource(hlsMediaSource)
                    _exoPlayer.prepare()
                    _exoPlayer.play()

                } catch (e: Exception) {
                    Log.e("PlayerViewModel", "Error fetching media: ${e.message}", e)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _exoPlayer.release()
        isPlayerReleased = true
    }
}

