package com.example.optimizeprimeandroidapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizeprimeandroidapp.R
import com.example.optimizeprimeandroidapp.databinding.FragmentMyFeedItemBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent.DummyItem

class MyProfileAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<MyProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FragmentMyFeedItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.fragment_my_feed_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (position == 0) {
            holder.binding.cvProfileInfo.visibility = VISIBLE
        } else{
            holder.binding.cvProfileInfo.visibility = GONE
        }
//        holder.idView.text = item.id
//        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: FragmentMyFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {

        override fun toString(): String {
            return ""
        }
    }
}