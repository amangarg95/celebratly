package com.example.optimizeprimeandroidapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.R
import com.example.optimizeprimeandroidapp.databinding.FragmentMyFeedBinding
import com.example.optimizeprimeandroidapp.dummy.DummyContent
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.example.optimizeprimeandroidapp.services.APIInterface
import com.example.optimizeprimeandroidapp.view.adapter.MyProfileAdapter
import com.example.optimizeprimeandroidapp.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MyFeedFragment : Fragment() {
    private lateinit var myFeedFragmentBinding: FragmentMyFeedBinding
    lateinit var apiInterface: APIInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFeedFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_feed, container, false)
        myFeedFragmentBinding.lifecycleOwner = this
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)

        myFeedFragmentBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        myFeedFragmentBinding.rvMyFeed.adapter = MyProfileAdapter(DummyContent.ITEMS, true)
        return myFeedFragmentBinding.root
    }


    private fun getOccurrences(){
        var recyclerDataArrayList: ArrayList<OccurrencesResponse>

        apiInterface.getOccurrences().enqueue(object :
            Callback<ArrayList<OccurrencesResponse>> {
            override fun onResponse(
                call: Call<ArrayList<OccurrencesResponse>>,
                response: Response<ArrayList<OccurrencesResponse>>
            ) {
                recyclerDataArrayList = response.body()!!

                Log.d(
                    "occurrence_test",
                    "recyclerDataArrayList-->" + recyclerDataArrayList.toString()
                )
            }

            override fun onFailure(
                call: Call<ArrayList<OccurrencesResponse>>,
                throwable: Throwable
            ) {
                Log.d("occurrence_test", "throwable-->" + throwable.message)

            }
        })
    }
}