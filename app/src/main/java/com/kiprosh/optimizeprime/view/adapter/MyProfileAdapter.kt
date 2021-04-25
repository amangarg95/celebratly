package com.kiprosh.optimizeprime.view.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedItemBinding
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.model.User


class MyProfileAdapter(
    private val user: User?,
    private val values: List<DummyContent.DummyItem>, var isMyFeed: Boolean
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
        val item = values[position]
        if (position == 0 && !isMyFeed) {
            holder.binding.cvProfileInfo.visibility = VISIBLE
            if (user != null) {
                holder.binding.tvName.text = user.fullName
                holder.binding.tvEmail.text = user.email
                holder.binding.tvDob.text = dateTimeUtil.changeDateFormat(user.dob)
                holder.binding.tvDoj.text = dateTimeUtil.changeDateFormat(user.doj)
            }
        } else {
            holder.binding.cvProfileInfo.visibility = GONE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: FragmentMyFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        override fun toString(): String {
            return ""
        }
    }
}