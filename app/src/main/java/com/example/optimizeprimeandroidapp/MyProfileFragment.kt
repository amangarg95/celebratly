package com.example.optimizeprimeandroidapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.databinding.FragmentMyProfileBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent

class MyProfileFragment : Fragment() {
    private lateinit var fragmentMyProfileBinding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMyProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        fragmentMyProfileBinding.lifecycleOwner = this

        fragmentMyProfileBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        fragmentMyProfileBinding.rvMyFeed.adapter = MyFeedItemRecyclerViewAdapter(DummyContent.ITEMS)
        return fragmentMyProfileBinding.root
    }
}