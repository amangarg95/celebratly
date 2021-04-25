package com.example.optimizeprimeandroidapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizeprimeandroidapp.R
import com.example.optimizeprimeandroidapp.dummy.DummyContent.DummyItem

class UpcomingEventsAdapter(
    private val values: List<DummyItem>, private val actionListener: ActionListener
) : RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_upcoming_event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.llMain.setOnClickListener { actionListener.onItemClick() }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain: LinearLayout = view.findViewById(R.id.ll_main)

        override fun toString(): String {
            return ""
        }
    }

    interface ActionListener {
        fun onItemClick()
    }
}