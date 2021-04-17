package com.example.optimizeprimeandroidapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.databinding.FragmentUpcomingEventsBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent
import com.example.optimizeprimeandroidapp.view.adapter.MyFeedItemRecyclerViewAdapter

class UpcomingEventsFragment : Fragment() {
    private lateinit var fragmentUpcomingEventsBinding: FragmentUpcomingEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUpcomingEventsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_events, container, false)
        fragmentUpcomingEventsBinding.lifecycleOwner = this

        fragmentUpcomingEventsBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        fragmentUpcomingEventsBinding.rvMyFeed.adapter = MyFeedItemRecyclerViewAdapter(DummyContent.ITEMS)
        return fragmentUpcomingEventsBinding.root
    }
}