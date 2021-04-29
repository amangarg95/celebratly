package com.kiprosh.optimizeprime.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentUpcomingEventItemBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.model.OccurrencesResponse

class UpcomingEventsAdapter(
    private val context: Context,
    private val values: List<OccurrencesResponse>, private val actionListener: ActionListener
) : RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FragmentUpcomingEventItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.fragment_upcoming_event_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val occurrencesResponse = values[position]
        if (occurrencesResponse.allowUpload) {
            holder.binding.tvAction.visibility = VISIBLE
            holder.binding.llMain.isClickable = true
            if (!occurrencesResponse.imageUrl.isNullOrEmpty()) {
                holder.binding.ivLock.setImageResource(R.drawable.ic_check)
                holder.binding.tvAction.text = "Tap on the card to preview your wishes"
                holder.binding.llMain.setOnClickListener {
                    actionListener.onItemClick(
                        occurrencesResponse,
                        occurrencesResponse.imageUrl
                    )
                }
            } else {
                holder.binding.tvAction.text = "Tap on the card to upload your wishes"
                holder.binding.llMain.setOnClickListener {
                    actionListener.onItemClick(
                        occurrencesResponse,
                        ""
                    )
                }
                holder.binding.ivLock.setImageResource(0)
            }
            holder.binding.llSchedule.visibility = VISIBLE
            holder.binding.tvTimeRemaining.text = occurrencesResponse.uploadTimeDistanceInWords
        } else {
            holder.binding.llMain.isClickable = false
            holder.binding.tvAction.visibility = GONE
            holder.binding.ivLock.setImageResource(R.drawable.ic_lock)
            holder.binding.llSchedule.visibility = INVISIBLE
        }

        if (AuthenticationHelper(context).getUser()!!.fullName.trimIndent() == occurrencesResponse.actionText) {
            holder.binding.llMain.isClickable = false
            holder.binding.tvAction.visibility = GONE
            holder.binding.ivLock.setImageResource(R.drawable.ic_lock)
            holder.binding.llSchedule.visibility = INVISIBLE
        }
        holder.binding.tvCardName.text = occurrencesResponse.title
        holder.binding.content.text = occurrencesResponse.caption
        holder.binding.tvDate.text = DateTimeUtil().changeDateFormat(occurrencesResponse.startAt)
        CommonCode(context).loadUserProfileImage(
            holder.binding.ivUserProfile,
            occurrencesResponse.photoUrl
        )
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: FragmentUpcomingEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        override fun toString(): String {
            return ""
        }
    }

    interface ActionListener {
        fun onItemClick(occurrencesResponse: OccurrencesResponse, imageUrl: String)
    }
}