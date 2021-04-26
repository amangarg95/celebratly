package com.kiprosh.optimizeprime.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter.Companion.playIndexThenPausePreviousPlayer
import app.videoplayerinsiderecyclerview.utils.RecyclerViewScrollListener
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedBinding
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.MyFeedAdapter
import com.kiprosh.optimizeprime.view.adapter.MyProfileAdapter
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MyFeedFragment : Fragment(), MyProfileAdapter.FullScreenListener {
    private lateinit var myFeedFragmentBinding: FragmentMyFeedBinding
    lateinit var apiInterface: APIInterface
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFeedFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_feed, container, false)
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
                recyclerDataArrayList = response.body()!!
                myFeedFragmentBinding.rvMyFeed.layoutManager = LinearLayoutManager(context)
                setAdapter(recyclerDataArrayList)
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

    override fun onToggleClick(isFullScreen: Boolean) {
    }

    private fun setAdapter(recyclerDataArrayList: ArrayList<OccurrencesResponse>) {
        val mAdapter: MyFeedAdapter =
            MyFeedAdapter(requireActivity(), null, recyclerDataArrayList, true)
        myFeedFragmentBinding.rvMyFeed!!.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(activity)
        myFeedFragmentBinding.rvMyFeed!!.layoutManager = layoutManager
        myFeedFragmentBinding.rvMyFeed!!.adapter = mAdapter
        var scrollListener: RecyclerViewScrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                // play just visible item
                if (index != -1)
                    playIndexThenPausePreviousPlayer(index)
            }

        }
        myFeedFragmentBinding.rvMyFeed!!.addOnScrollListener(scrollListener)
        mAdapter!!.SetOnItemClickListener(object : MyFeedAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: OccurrencesResponse?) {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }

}