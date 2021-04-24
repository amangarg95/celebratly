package com.example.optimizeprimeandroidapp.view.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.R
import com.example.optimizeprimeandroidapp.view.activity.UploadDataActivity
import com.example.optimizeprimeandroidapp.databinding.FragmentUpcomingEventsBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent
import com.example.optimizeprimeandroidapp.view.adapter.UpcomingEventsAdapter


class UpcomingEventsFragment : Fragment(), UpcomingEventsAdapter.ActionListener {
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
        fragmentUpcomingEventsBinding.rvMyFeed.adapter =
            UpcomingEventsAdapter(DummyContent.ITEMS, this)
        //callSampleAPI()
        return fragmentUpcomingEventsBinding.root
    }

    override fun onItemClick() {
        val intent = Intent(this.context, UploadDataActivity::class.java)
        startActivity(intent)
    }

//    fun callSampleAPI() {
//        val service = RetrofitClientInstance.getRetrofitInstance().create(
//            GetDataService::class.java
//        )
//
//        val call: Call<UserProfile> = service.getUser("login.json", "karan.valecha@kiprosh.com")
//        call.enqueue(object : Callback<UserProfile> {
//            override fun onResponse(
//                call: Call<UserProfile>,
//                response: Response<UserProfile>
//            ) {
//                Log.v("Hello", response.body()!!.url)
//            }
//
//            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
//            }
//        })
//    }

//    fun callAPIForUserList() {
//        val service = RetrofitClientInstance.getRetrofitInstance().create(
//            GetDataService::class.java
//        )
//
//        val call: Call<List<User>> = service.userList
//        call.enqueue(object : Callback<List<User>> {
//            override fun onResponse(
//                call: Call<List<User>>,
//                response: Response<List<User>>
//            ) {
//                response.body()?.forEach {
//                    Log.v("Hello", it.fullName)
//                }
//            }
//
//            override fun onFailure(call: Call<List<User>>, t: Throwable) {
//            }
//        })
//    }
}