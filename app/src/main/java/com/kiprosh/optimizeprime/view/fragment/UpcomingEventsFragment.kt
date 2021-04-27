package com.kiprosh.optimizeprime.view.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentUpcomingEventsBinding
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.activity.UploadDataActivity
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import com.kiprosh.optimizeprime.view.adapter.UpcomingEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UpcomingEventsFragment : Fragment(), UpcomingEventsAdapter.ActionListener {
    private lateinit var fragmentUpcomingEventsBinding: FragmentUpcomingEventsBinding
    lateinit var apiInterface: APIInterface
    lateinit var progressDialog: ProgressDialog
    var listByWeek = ArrayList<OccurrencesResponse>()
    var listByMonth = ArrayList<OccurrencesResponse>()
    var listByYear = ArrayList<OccurrencesResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUpcomingEventsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_events, container, false)
        fragmentUpcomingEventsBinding.lifecycleOwner = this
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        progressDialog = ProgressDialog()
        updateStatusBarColour()
        getOccurrences()
        fragmentUpcomingEventsBinding.sbgFilter.setOnClickedButtonListener {
            when (it) {
                0 -> {
                    setAdapter(listByWeek)
                }
                1 -> {
                    setAdapter(listByMonth)
                }
                2 -> {
                    setAdapter(listByYear)
                }
            }
        }
        return fragmentUpcomingEventsBinding.root
    }

    override fun onItemClick() {
        val intent = Intent(this.context, UploadDataActivity::class.java)
        startActivity(intent)
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

                recyclerDataArrayList.forEach {
                    if (DateTimeUtil().getDifferenceInDate(
                            Calendar.getInstance().time,
                            SimpleDateFormat("yyyy-MM-dd").parse(it.startAt)
                        ).first < 8
                    ) {
                        listByWeek.add(it)
                    } else if (DateTimeUtil().getDifferenceInDate(
                            Calendar.getInstance().time,
                            SimpleDateFormat("yyyy-MM-dd").parse(it.startAt)
                        ).first in 8..31
                    ) {
                        listByMonth.add(it)
                    } else {
                        listByYear.add(it)
                    }
                }
                setAdapter(listByWeek)
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

    private fun setAdapter(recyclerDataArrayList: ArrayList<OccurrencesResponse>) {
        val mAdapter =
            UpcomingEventsAdapter(recyclerDataArrayList, this)
        fragmentUpcomingEventsBinding.rvMyFeed.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        fragmentUpcomingEventsBinding.rvMyFeed.layoutManager = layoutManager
        fragmentUpcomingEventsBinding.rvMyFeed.adapter = mAdapter
    }

    private fun updateStatusBarColour() {
        val window: Window = activity!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.baby_pink)
    }
}
