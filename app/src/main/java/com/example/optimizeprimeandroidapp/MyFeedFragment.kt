package com.example.optimizeprimeandroidapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.databinding.FragmentMyFeedBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent

class MyFeedFragment : Fragment() {
    private lateinit var myFeedFragmentBinding: FragmentMyFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFeedFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_feed, container, false)
        myFeedFragmentBinding.lifecycleOwner = this

        myFeedFragmentBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        myFeedFragmentBinding.rvMyFeed.adapter = MyFeedItemRecyclerViewAdapter(DummyContent.ITEMS)
        return myFeedFragmentBinding.root
    }
}