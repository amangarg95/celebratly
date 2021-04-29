package com.kiprosh.optimizeprime.view.adapter

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentUpcomingEventItemBinding
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.model.OccurrencesResponse
import java.util.*

class UpcomingEventsAdapter(
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
//        if (occurrencesResponse.allowUpload) {
//            holder.binding.llMain.isClickable = true
//            if (!occurrencesResponse.imageUrl.isNullOrEmpty()) {
//                holder.binding.ivLock.setImageResource(R.drawable.ic_check)
//                holder.binding.llMain.setOnClickListener {
//                    actionListener.onItemClick(
//                        occurrencesResponse,
//                        occurrencesResponse.imageUrl
//                    )
//                }
//            } else {
//                holder.binding.llMain.setOnClickListener {
//                    actionListener.onItemClick(
//                        occurrencesResponse,
//                        ""
//                    )
//                }
//                holder.binding.ivLock.setImageResource(0)
//            }
//        } else {
//            holder.binding.llMain.isClickable = false
//            holder.binding.ivLock.setImageResource(R.drawable.ic_lock)
//        }

        if (!occurrencesResponse.imageUrl.isNullOrEmpty()) {
            holder.binding.ivLock.setImageResource(R.drawable.ic_check)
            holder.binding.tvAction.visibility = VISIBLE
            holder.binding.tvAction.text = "Click on the card to preview your wishes!"
            holder.binding.llMain.setOnClickListener {
                actionListener.onItemClick(
                    occurrencesResponse,
                    occurrencesResponse.imageUrl
                )
            }
        } else {
            holder.binding.tvAction.visibility = VISIBLE
            holder.binding.tvAction.text = "Click on the card to upload your wishes!"
            holder.binding.llMain.setOnClickListener {
                actionListener.onItemClick(
                    occurrencesResponse,
                    ""
                )
            }
            holder.binding.ivLock.setImageResource(0)
        }


        holder.binding.tvCardName.text = occurrencesResponse.title
        holder.binding.content.text = occurrencesResponse.caption
        holder.binding.tvDate.text = DateTimeUtil().changeDateFormat(occurrencesResponse.startAt)

        val calendar = Calendar.getInstance()
        calendar.time =
            DateTimeUtil().getDateFromString(occurrencesResponse.startAt)
        calendar.add(Calendar.HOUR, 10)

        val diffInDays = DateTimeUtil().getDifferenceInDate(
            Calendar.getInstance().time,
            calendar.time
        ).first.toInt()

        var diffInHours = DateTimeUtil().getDifferenceInDate(
            Calendar.getInstance().time,
            calendar.time
        ).second.toInt()

        for (index in 0..diffInDays) {
            if (diffInDays != 0) {
                diffInHours += 24
            }
        }
        if (diffInDays != 0) {
            diffInHours -= 24
        }
        holder.binding.tvTimeRemaining.text =
            if (diffInHours == 1) "$diffInHours hour remaining to wish!" else "$diffInHours hours remaining to wish!"
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