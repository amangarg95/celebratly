package com.example.optimizeprimeandroidapp.view.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.optimizeprimeandroidapp.model.UpcomingEventsResponse
import com.example.optimizeprimeandroidapp.services.APIInterface
import com.example.optimizeprimeandroidapp.view.adapter.RetrofitClientInstance
import com.example.optimizeprimeandroidapp.view.adapter.UpcomingEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class UpcomingEventsFragment : Fragment(), UpcomingEventsAdapter.ActionListener {
    private lateinit var fragmentUpcomingEventsBinding: FragmentUpcomingEventsBinding
    lateinit var apiInterface: APIInterface

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
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)

        return fragmentUpcomingEventsBinding.root
    }

    override fun onItemClick() {
        val intent = Intent(this.context, UploadDataActivity::class.java)
        startActivity(intent)
    }

    private fun getUpcomingEvents(){
        var recyclerDataArrayList: ArrayList<UpcomingEventsResponse>

        apiInterface.getUpcomingEvents().enqueue(object :
            Callback<ArrayList<UpcomingEventsResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UpcomingEventsResponse>>,
                response: Response<ArrayList<UpcomingEventsResponse>>
            ) {
                recyclerDataArrayList = response.body()!!
                Log.d(
                    "upcoming_test",
                    "recyclerDataArrayList-->" + recyclerDataArrayList.toString()
                )
            }

            override fun onFailure(
                call: Call<ArrayList<UpcomingEventsResponse>>,
                throwable: Throwable
            ) {
                Log.d("upcoming_test", "throwable-->" + throwable.message)

            }
        })
    }
}