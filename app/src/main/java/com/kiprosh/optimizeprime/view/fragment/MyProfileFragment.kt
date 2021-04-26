package com.kiprosh.optimizeprime.view.fragment

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyProfileBinding
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.view.adapter.MyProfileAdapter

class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding

    private var player: SimpleExoPlayer? = null
    private lateinit var exoFullScreenToggle: ImageView
    private var playerView: PlayerView? = null
    private lateinit var authenticationHelper: AuthenticationHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        binding.lifecycleOwner = this
        authenticationHelper = AuthenticationHelper(activity!!.applicationContext)
        binding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        val user = authenticationHelper.getUser()
        binding.rvMyFeed.adapter = MyProfileAdapter(
            user,
            DummyContent.ITEMS,
            false
        )
        updateStatusBarColour()
        CommonCode(context!!).loadUserProfileImage(binding.ivUserProfile, user?.profileUrl)
        /*exoFullScreenToggle = fragmentMyProfileBinding.exoPlayerView.exo_full_screen_toggle
        playerView = fragmentMyProfileBinding.exoPlayerView
        rlPlayer = fragmentMyProfileBinding.rlPlayer*/
        return binding.root
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

    private fun updateStatusBarColour() {
        val window: Window = activity!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.yellow)
    }
}