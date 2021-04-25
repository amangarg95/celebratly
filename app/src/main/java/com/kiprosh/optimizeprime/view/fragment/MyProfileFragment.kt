package com.example.optimizeprimeandroidapp.view.fragment

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.R
import com.example.optimizeprimeandroidapp.databinding.FragmentMyProfileBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent
import com.example.optimizeprimeandroidapp.helper.CommonCode
import com.example.optimizeprimeandroidapp.view.adapter.MyProfileAdapter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class MyProfileFragment : Fragment() {
    private lateinit var fragmentMyProfileBinding: FragmentMyProfileBinding

    private var player: SimpleExoPlayer? = null
    private lateinit var exoFullScreenToggle: ImageView
    private var playerView: PlayerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMyProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        fragmentMyProfileBinding.lifecycleOwner = this

        fragmentMyProfileBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        fragmentMyProfileBinding.rvMyFeed.adapter = MyProfileAdapter(DummyContent.ITEMS, false)

        CommonCode(context!!).loadUserProfileImage(fragmentMyProfileBinding.ivUserProfile, "")
        /*exoFullScreenToggle = fragmentMyProfileBinding.exoPlayerView.exo_full_screen_toggle
        playerView = fragmentMyProfileBinding.exoPlayerView
        rlPlayer = fragmentMyProfileBinding.rlPlayer*/
        return fragmentMyProfileBinding.root
    }

    private fun configureMediaPlayer(videoUrl: String) {
        //exoFullScreenToggle.setOnClickListener(this)
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        playerView?.player = player
        playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, getString(R.string.app_name))
        )
        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl))
        player?.prepare(mediaSource)

        player?.addListener(
            object : Player.EventListener {
                override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

                override fun onRepeatModeChanged(repeatMode: Int) {}

                override fun onPositionDiscontinuity(reason: Int) {}

                override fun onLoadingChanged(isLoading: Boolean) {}

                override fun onTracksChanged(
                    trackGroups: TrackGroupArray?,
                    trackSelections: TrackSelectionArray?
                ) {

                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}

                override fun onPlayerError(error: ExoPlaybackException?) {
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                }

                override fun onSeekProcessed() {}
            })
        val mediaSession = context?.let { MediaSessionCompat(it, it.packageName) }
        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player, null)
        mediaSession?.isActive = true
    }
}