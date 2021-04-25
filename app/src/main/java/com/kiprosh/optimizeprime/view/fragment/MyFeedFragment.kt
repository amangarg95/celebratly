package com.kiprosh.optimizeprime.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedBinding
import com.kiprosh.optimizeprime.view.adapter.MyProfileAdapter


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
        myFeedFragmentBinding.rvMyFeed.adapter = MyProfileAdapter(DummyContent.ITEMS, true)
        return myFeedFragmentBinding.root
    }
}