package com.example.optimizeprimeandroidapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.databinding.FragmentMyEventsBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent

class MyEventsFragment : Fragment() {
    private lateinit var fragmentMyEventsBinding: FragmentMyEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMyEventsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_events, container, false)
        fragmentMyEventsBinding.lifecycleOwner = this

        fragmentMyEventsBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        fragmentMyEventsBinding.rvMyFeed.adapter = MyFeedItemRecyclerViewAdapter(DummyContent.ITEMS)
        return fragmentMyEventsBinding.root
    }
}