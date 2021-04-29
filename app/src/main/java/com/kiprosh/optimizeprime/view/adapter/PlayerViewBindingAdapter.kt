package com.kiprosh.optimizeprime.view.adapter

import android.content.Context
import android.net.Uri
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.PlayerStateCallback
import kotlinx.android.synthetic.main.home_player_control.view.*

// extension function for show toast
fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

class PlayerViewAdapter {

    companion object {
        // for hold all players generated
        private var playersMap: MutableMap<Int, SimpleExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideo: Pair<Int, SimpleExoPlayer>? = null
        fun releaseAllPlayers() {
            playersMap.map {
                it.value.release()
            }
        }

        // call when item recycled to improve performance
        fun releaseRecycledPlayers(index: Int) {
            playersMap[index]?.release()
        }

        // call when scroll to pause any playing player
        fun pauseCurrentPlayingVideo() {
            if (currentPlayingVideo != null) {
                currentPlayingVideo?.second?.playWhenReady = false
            }
        }

        fun playIndexThenPausePreviousPlayer(index: Int) {
            if (playersMap.get(index)?.playWhenReady == false) {
                pauseCurrentPlayingVideo()
                playersMap.get(index)?.playWhenReady = true
                currentPlayingVideo = Pair(index, playersMap.get(index)!!)
            }

        }

        /*
        *  url is a url of stream video
        *  progressbar for show when start buffering stream
        * thumbnail for show before video start
        * */
        @JvmStatic
        @BindingAdapter(
            value = ["video_url", "thumbnail_url", "on_state_change", "progressbar", "thumbnail", "item_index"],
            requireAll = false
        )
        fun PlayerView.loadVideo(
            url: String,
            thumbnailUrl: String,
            callback: PlayerStateCallback,
            progressbar: ProgressBar,
            thumbnail: AppCompatImageView,
            item_index: Int? = null
        ) {
            if (url == null) return
            val player = SimpleExoPlayer.Builder(context).build()
            CommonCode(context).loadImageWithUrl(thumbnailUrl, 0, this@loadVideo.iv_exo_player_thumbnail)

            player.playWhenReady = false
            player.repeatMode = Player.REPEAT_MODE_ALL
            // When changing track, retain the latest frame instead of showing a black screen
            setKeepContentOnPlayerReset(true)
            // We'll show the controller, change to true if want controllers as pause and start
            this.useController = true
            //this.controllerAutoShow = false
            this.controllerShowTimeoutMs = 2500
            // Provide url to load the video from here
            val mediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory("Demo"))
                .createMediaSource(Uri.parse(url))

            player.prepare(mediaSource)

            this.player = player

            // add player with its index to map
            if (playersMap.containsKey(item_index))
                playersMap.remove(item_index)
            if (item_index != null)
                playersMap[item_index] = player

            this.player!!.addListener(object : Player.EventListener {
                override fun onTracksChanged(
                    trackGroups: TrackGroupArray,
                    trackSelections: TrackSelectionArray
                ) {
                    super.onTracksChanged(trackGroups, trackSelections)
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    //this@loadVideo.context.toast("Oops! Error occurred while playing media.")
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)

                    if (playbackState == Player.STATE_BUFFERING) {
                        callback.onVideoBuffering(player)
                        // Buffering..
                        // set progress bar visible here
                        // set thumbnail visible
                        //thumbnail.visibility = View.VISIBLE
                        //progressbar.visibility = View.VISIBLE
                    }

                    if (playbackState == Player.STATE_READY) {
                        // [PlayerView] has fetched the video duration so this is the block to hide the buffering progress bar
                        //progressbar.visibility = View.GONE
                        // set thumbnail gone
                        //thumbnail.visibility = View.GONE
                        callback.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                    }

                    if (playbackState == Player.STATE_READY && player.playWhenReady) {
                        // [PlayerView] has started playing/resumed the video
                        this@loadVideo.iv_exo_player_thumbnail.visibility = GONE
                        callback.onStartedPlaying(player)
                    }
                }
            })
        }
    }
}