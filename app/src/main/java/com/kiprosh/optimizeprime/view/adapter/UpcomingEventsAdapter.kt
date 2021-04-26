package com.kiprosh.optimizeprime.view.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentUpcomingEventItemBinding

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
        val item = values[position]
        holder.binding.llMain.setOnClickListener { actionListener.onItemClick() }
        if (position == 0) {
            holder.binding.ivLock.visibility = GONE
        }
        holder.binding.tvCardName.text = item.titleText
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: FragmentUpcomingEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        override fun toString(): String {
            return ""
        }
    }

    interface ActionListener {
        fun onItemClick()
    }
}