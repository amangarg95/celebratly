package com.kiprosh.optimizeprime.view.adapter

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.google.android.exoplayer2.*
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
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedItemBinding
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.model.User
import kotlinx.android.synthetic.main.home_player_control.view.*
import java.util.ArrayList


class MyProfileAdapter(
    private val listener: FullScreenListener,
    private val context: Context,
    private val user: User?,
    private val occurrenceList: ArrayList<OccurrencesResponse>, var isMyFeed: Boolean
) : RecyclerView.Adapter<MyProfileAdapter.ViewHolder>() {

    private val dateTimeUtil = DateTimeUtil()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FragmentMyFeedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_my_feed_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = occurrenceList[position]

        if (!isMyFeed) {
            if (position == 0) {
                holder.binding.cvProfileInfo.visibility = VISIBLE
                if (user != null) {
                    holder.binding.tvName.text = user.fullName
                    holder.binding.tvEmail.text = user.email
                    holder.binding.tvDob.text = dateTimeUtil.changeDateFormat(user.dob)
                    holder.binding.tvDoj.text = dateTimeUtil.changeDateFormat(user.doj)
                }
            } else {
                updateCards(item, holder)
            }

        } else {
            holder.binding.cvProfileInfo.visibility = GONE
            updateCards(item, holder)
        }
        configureMediaPlayer("", holder)
    }

    private fun updateCards(item: OccurrencesResponse, holder: MyProfileAdapter.ViewHolder) {
        holder.binding.date.text = dateTimeUtil.changeDateFormat(item.startAt)
        if (item.eventTyoe == "work_anniversary") {
            holder.binding.content.text = item.titleText + "\n" + item.caption
        } else {
            holder.binding.content.text = item.titleText
        }
    }

    override fun getItemCount(): Int = occurrenceList.size

    inner class ViewHolder(val binding: FragmentMyFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        override fun toString(): String {
            return ""
        }
    }

    interface FullScreenListener {
        fun onToggleClick(isFullScreen: Boolean)
    }

    private fun configureMediaPlayer(videoUrl: String, holder: ViewHolder) {
        /*var player: SimpleExoPlayer? = null

        var exoFullScreenToggle: ImageView = holder.binding.exoPlayerView.exo_full_screen_toggle
        var playerView: PlayerView = holder.binding.exoPlayerView

        //configureMediaPlayer("https://player.vimeo.com/external/481511608.hd.mp4?s=40bfbf85159679a2f69f1155f9ae4d6da357580b")
        exoFullScreenToggle.setOnClickListener {

        }
        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        playerView?.player = player
        playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context.getString(R.string.app_name))
        )
        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse("https://player.vimeo.com/external/481511608.hd.mp4?s=40bfbf85159679a2f69f1155f9ae4d6da357580b"))
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
        val mediaSession = context.let { MediaSessionCompat(it, it.packageName) }
        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player, null)
        mediaSession.isActive = true*/
    }
}