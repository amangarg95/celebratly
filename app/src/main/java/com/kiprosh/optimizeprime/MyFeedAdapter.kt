package com.kiprosh.optimizeprime

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.videoplayerinsiderecyclerview.utils.PlayerStateCallback
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter.Companion.releaseRecycledPlayers
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.google.android.exoplayer2.Player
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedItemBinding
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import java.util.*

class MyFeedAdapter(
    private val mContext: Context,
    private var modelList: ArrayList<OccurrencesResponse>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {
    private val dateTimeUtil = DateTimeUtil()

    private var mItemClickListener: OnItemClickListener? =
        null

    fun updateList(modelList: ArrayList<OccurrencesResponse>) {
        this.modelList = modelList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): VideoPlayerViewHolder {
        val binding: FragmentMyFeedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context), R.layout.fragment_my_feed_item, viewGroup, false
        )
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //Here you can fill your row view
        if (holder is VideoPlayerViewHolder) {
            val model = getItem(position)
            val genericViewHolder = holder

            // send data to view holder
            genericViewHolder.onBind(model)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    private fun getItem(position: Int): OccurrencesResponse {
        return modelList[position]
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(
            view: View?,
            position: Int,
            model: OccurrencesResponse?
        )
    }

    inner class VideoPlayerViewHolder(private val binding: FragmentMyFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            model: OccurrencesResponse
        ) {

            binding.cvProfileInfo.visibility = View.GONE
            binding.date.text = dateTimeUtil.changeDateFormat(model.startAt)
            if (model.eventTyoe == "work_anniversary") {
                binding.content.text = model.titleText + "\n" + model.caption
            } else {
                binding.content.text = model.titleText
            }

            // handel on item click
            binding.root.setOnClickListener {
                mItemClickListener!!.onItemClick(
                    it,
                    adapterPosition,
                    model
                )
            }
            var videoPlayerUrl = model.videoUrl
            Log.d("sadajhsjd", "videoPlayerUrl-->" + videoPlayerUrl)
            if (videoPlayerUrl.isNullOrEmpty()) {
                videoPlayerUrl =
                    "https://player.vimeo.com/external/481511608.hd.mp4?s=40bfbf85159679a2f69f1155f9ae4d6da357580b"
            }
            Log.d("sadajhsjd", "New videoPlayerUrl-->" + videoPlayerUrl)

            binding.apply {
                videoUrl = videoPlayerUrl
                callback = this@MyFeedAdapter
                index = adapterPosition
                executePendingBindings()
            }


        }
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
        Log.d("playvideo", "onVideoDurationRetrieved")
    }

    override fun onVideoBuffering(player: Player) {
        Log.d("playvideo", "onVideoBuffering")
    }

    override fun onStartedPlaying(player: Player) {
        Log.d("playvideo", "staaaart" + player.contentDuration)

    }


    override fun onFinishedPlaying(player: Player) {
        Log.d("playvideo", "onFinishedPlaying")
    }
}