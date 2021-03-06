package com.kiprosh.optimizeprime.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedItemBinding
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.helper.PlayerStateCallback
import com.kiprosh.optimizeprime.model.OccurrencesResponse
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter.Companion.releaseRecycledPlayers
import java.util.*

class FeedAdapter(
    private val mContext: Context,
    private val user: User?,
    private var modelList: ArrayList<OccurrencesResponse>,
    private var isMyFeed: Boolean,
    private val onClickListener: OnClickListener,

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
            if (!modelList.isEmpty()) {
                if (!isMyFeed) {
                    if (position == 0) {
                        CommonCode(mContext).loadUserProfileImage(
                            holder.binding.ivUserProfile,
                            user!!.profileUrl
                        )
                        holder.binding.llPic.visibility = View.VISIBLE
                        holder.binding.cvProfileInfo.visibility = View.VISIBLE
                        holder.binding.tvMyFeed.visibility = View.VISIBLE
                        if (user != null) {
                            holder.binding.tvName.text = user.fullName
                            holder.binding.tvEmail.text = user.email
                            holder.binding.tvDob.text = dateTimeUtil.changeDateFormat(user.dob)
                            holder.binding.tvDoj.text = dateTimeUtil.changeDateFormat(user.doj)
                        }
                        updateCards(model, holder.binding)
                    } else {
                        holder.binding.llPic.visibility = GONE
                        holder.binding.cvProfileInfo.visibility = GONE
                        holder.binding.tvMyFeed.visibility = GONE
                        updateCards(model, holder.binding)
                    }
                    holder.binding.ivShareText.visibility = View.VISIBLE
                    holder.binding.ivShareText.setOnClickListener {
                        onClickListener.onShareClick(
                            model.finalVideoUrl
                        )
                    }
                } else {
                    holder.binding.llPic.visibility = GONE
                    holder.binding.cvProfileInfo.visibility = GONE
                    holder.binding.tvMyFeed.visibility = GONE
                    holder.binding.ivShareText.visibility = GONE
                    updateCards(model, holder.binding)
                }
                // send data to view holder
                genericViewHolder.onBind(model, holder)
            } else {
                if (!isMyFeed) {
                    if (position == 0) {
                        CommonCode(mContext).loadUserProfileImage(
                            holder.binding.ivUserProfile,
                            user!!.profileUrl
                        )
                        holder.binding.llPic.visibility = View.VISIBLE
                        holder.binding.cvProfileInfo.visibility = View.VISIBLE
                        holder.binding.tvMyFeed.visibility = View.VISIBLE
                        if (user != null) {
                            holder.binding.tvName.text = user.fullName
                            holder.binding.tvEmail.text = user.email
                            holder.binding.tvDob.text = dateTimeUtil.changeDateFormat(user.dob)
                            holder.binding.tvDoj.text = dateTimeUtil.changeDateFormat(user.doj)
                        }
                        holder.binding.cvPlayer.visibility = GONE
                        holder.binding.tvMyFeed.text =
                            "---------  Your memories will appear here  ---------"

                        holder.binding.tvAboutCelebratly.setOnClickListener { onClickListener.onAboutCelebratlyClick() }
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return if (modelList.isEmpty()) {
            1
        } else {
            modelList.size
        }
    }

    private fun getItem(position: Int): OccurrencesResponse {
        return if (modelList.isEmpty()) {
            OccurrencesResponse(
                true, "", "", "", 1, "", "", "", 1, "", "",
                mutableListOf(), "", "", "", "", 0L, "", ""
            )
        } else {
            modelList[position]
        }
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(
            view: View?,
            position: Int,
            model: OccurrencesResponse?
        )
    }

    interface OnClickListener {
        fun onShareClick(link: String)
        fun onAboutCelebratlyClick()
    }

    inner class VideoPlayerViewHolder(val binding: FragmentMyFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: OccurrencesResponse, holder: RecyclerView.ViewHolder) {

            // handel on item click
            binding.root.setOnClickListener {
                mItemClickListener!!.onItemClick(
                    it,
                    adapterPosition,
                    model
                )
            }
            var videoPlayerUrl = model.finalVideoUrl
            Log.d("test_video_url", "videoPlayerUrl-->" + videoPlayerUrl)
            if (videoPlayerUrl.isNullOrEmpty()) {
                videoPlayerUrl = ""
            }

            binding.apply {
                thumbnailUrl = videoPlayerUrl.replace(".mp4", ".jpg")
                videoUrl = videoPlayerUrl
                callback = this@FeedAdapter
                index = adapterPosition
                executePendingBindings()
            }


        }
    }

    private fun updateCards(model: OccurrencesResponse, binding: FragmentMyFeedItemBinding) {
        binding.date.text = dateTimeUtil.changeDateFormat(model.startAt)
        if (model.eventType == "work_anniversary") {
            binding.content.text = model.title + "\n" + model.caption
        } else {
            binding.content.text = model.title
        }
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
    }

    override fun onVideoBuffering(player: Player) {
    }

    override fun onStartedPlaying(player: Player) {
    }

    override fun onFinishedPlaying(player: Player) {
    }
}