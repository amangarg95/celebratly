package com.kiprosh.optimizeprime.view.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentUpcomingEventsBinding
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.model.UpcomingEventsResponse
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.activity.UploadDataActivity
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import com.kiprosh.optimizeprime.view.adapter.UpcomingEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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