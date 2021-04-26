package com.kiprosh.optimizeprime.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedBinding
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.MyProfileAdapter
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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
        Log.d("occurrence_test", "MyFeedFragment")
//        getOccurrences()
        updateStatusBarColour()
        myFeedFragmentBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        myFeedFragmentBinding.rvMyFeed.adapter =
            MyProfileAdapter(null, DummyContent.ITEMS, true, null)
        return myFeedFragmentBinding.root
    }


    private fun getOccurrences() {
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

    private fun updateStatusBarColour() {
        val window: Window = activity!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.light_blue)
    }

}