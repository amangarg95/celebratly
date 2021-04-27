package com.kiprosh.optimizeprime.view.fragment

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiprosh.optimizeprime.model.OccurrencesResponse
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentFeedBinding
import com.kiprosh.optimizeprime.helper.FeedHelper
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.helper.RecyclerViewScrollListener
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.FeedAdapter
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter.Companion.playIndexThenPausePreviousPlayer
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FeedFragment : Fragment(), FeedAdapter.OnShareClickListener {
    private lateinit var myFeedFragmentBinding: FragmentFeedBinding
    lateinit var apiInterface: APIInterface
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFeedFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        myFeedFragmentBinding.lifecycleOwner = this
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        progressDialog = ProgressDialog()
        getOccurrences()
        updateStatusBarColour()
        return myFeedFragmentBinding.root
    }

    private fun getOccurrences() {
        progressDialog.showProgress(fragmentManager)
        var recyclerDataArrayList: ArrayList<OccurrencesResponse>
        apiInterface.getOccurrences().enqueue(object :
            Callback<ArrayList<OccurrencesResponse>> {
            override fun onResponse(
                call: Call<ArrayList<OccurrencesResponse>>,
                response: Response<ArrayList<OccurrencesResponse>>
            ) {
                if (response.isSuccessful) {
                    val feedHelper = FeedHelper()
                    recyclerDataArrayList = response.body()!!
                    val eventList = feedHelper.sortList(recyclerDataArrayList)
                    recyclerDataArrayList.clear()
                    recyclerDataArrayList.addAll(eventList)
                    myFeedFragmentBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
                    setAdapter(recyclerDataArrayList)
                }
                progressDialog.hideProgress()
            }

            override fun onFailure(
                call: Call<ArrayList<OccurrencesResponse>>,
                throwable: Throwable
            ) {
                progressDialog.hideProgress()
            }
        })
    }

    private fun updateStatusBarColour() {
        val window: Window = activity!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.light_blue)
    }

    private fun setAdapter(recyclerDataArrayList: ArrayList<OccurrencesResponse>) {
        val mAdapter =
            FeedAdapter(requireActivity(), null, recyclerDataArrayList, true, this)
        myFeedFragmentBinding.rvMyFeed.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        myFeedFragmentBinding.rvMyFeed.layoutManager = layoutManager
        myFeedFragmentBinding.rvMyFeed.adapter = mAdapter
        val scrollListener: RecyclerViewScrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                // play just visible item
                if (index != -1)
                    playIndexThenPausePreviousPlayer(index)
            }

        }
        myFeedFragmentBinding.rvMyFeed.addOnScrollListener(scrollListener)
        mAdapter.setOnItemClickListener(object : FeedAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: OccurrencesResponse?) {
                // Do nothing
            }
        })
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }

    override fun onShareClick(link: String) {
        //Do Nothing
    }
}