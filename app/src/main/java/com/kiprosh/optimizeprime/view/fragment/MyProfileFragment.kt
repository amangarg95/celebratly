package com.kiprosh.optimizeprime.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter
import app.videoplayerinsiderecyclerview.utils.RecyclerViewScrollListener
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyProfileBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.MyFeedAdapter
import com.kiprosh.optimizeprime.view.adapter.MyProfileAdapter
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MyProfileFragment : Fragment(), MyProfileAdapter.FullScreenListener {
    private lateinit var binding: FragmentMyProfileBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var apiInterface: APIInterface
    private lateinit var authenticationHelper: AuthenticationHelper
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        binding.lifecycleOwner = this
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        progressDialog = ProgressDialog()
        authenticationHelper = AuthenticationHelper(activity!!.applicationContext)
        binding.rvMyFeed.layoutManager = LinearLayoutManager(context)
        user = authenticationHelper.getUser()
        getOccurrences()

        updateStatusBarColour()
        CommonCode(context!!).loadUserProfileImage(binding.ivUserProfile, "https://homepages.cae.wisc.edu/~ece533/images/lena.png")
        return binding.root
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
                Log.d(
                    "occurrence_test",
                    "recyclerDataArrayList-->" + recyclerDataArrayList.toString()
                )

                setAdapter(recyclerDataArrayList)
                //binding.rvMyFeed.adapter = MyProfileAdapter(this@MyProfileFragment, context!!, user, recyclerDataArrayList, false)
                progressDialog.hideProgress()
            }

            override fun onFailure(
                call: Call<ArrayList<OccurrencesResponse>>,
                throwable: Throwable
            ) {
                progressDialog.hideProgress()
                Log.d("occurrence_test", "throwable-->" + throwable.message)

            }
        })
    }

    private fun updateStatusBarColour() {
        val window: Window = activity!!.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.pistachio_green)
    }

    override fun onToggleClick(isFullScreen: Boolean) {

    }

    private fun setAdapter(recyclerDataArrayList: ArrayList<OccurrencesResponse>) {
        val mAdapter: MyFeedAdapter =
            MyFeedAdapter(requireActivity(), user, recyclerDataArrayList, false)
        binding.rvMyFeed!!.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(activity)
        binding.rvMyFeed!!.layoutManager = layoutManager
        binding.rvMyFeed!!.adapter = mAdapter
        var scrollListener: RecyclerViewScrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                Log.d("visible item index", index.toString())
                // play just visible item
                if (index != -1)
                    PlayerViewAdapter.playIndexThenPausePreviousPlayer(index)
            }

        }
        binding.rvMyFeed!!.addOnScrollListener(scrollListener)
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