package com.kiprosh.optimizeprime.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyProfileBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.helper.RecyclerViewScrollListener
import com.kiprosh.optimizeprime.model.OccurrencesResponse
import com.kiprosh.optimizeprime.model.ProfileAndOccurrencesResponse
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.activity.AboutCelebratlyActivity
import com.kiprosh.optimizeprime.view.activity.SignInActivity
import com.kiprosh.optimizeprime.view.adapter.FeedAdapter
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MyProfileFragment : Fragment(), FeedAdapter.OnClickListener {
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
        binding.tvLogOut.setOnClickListener {
            initiateLogOut()
        }
        user = authenticationHelper.getUser()
        getProfileWithOccurrences()
        updateStatusBarColour()
        return binding.root
    }

    private fun getProfileWithOccurrences() {
        progressDialog.showProgress(fragmentManager)
        var recyclerDataArrayList: ArrayList<OccurrencesResponse>
        val headerMap = authenticationHelper.getHeaderMap()

        apiInterface.getProfileWithOccurrences(headerMap!!).enqueue(object :
            Callback<ProfileAndOccurrencesResponse> {
            override fun onResponse(
                call: Call<ProfileAndOccurrencesResponse>,
                response: Response<ProfileAndOccurrencesResponse>
            ) {
                if (response.isSuccessful) {
                    recyclerDataArrayList = response.body()?.listOfOccurrences!!

//                    val eventList = feedHelper.sortList(recyclerDataArrayList)
//                    recyclerDataArrayList.clear()
//                    recyclerDataArrayList.addAll(eventList)
                    setAdapter(recyclerDataArrayList)
                }
                progressDialog.hideProgress()
            }

            override fun onFailure(
                call: Call<ProfileAndOccurrencesResponse>,
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
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.pistachio_green)
    }

    private fun setAdapter(recyclerDataArrayList: ArrayList<OccurrencesResponse>) {
        val mAdapter =
            FeedAdapter(requireActivity(), user, recyclerDataArrayList, false, this)
        binding.rvMyFeed.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        binding.rvMyFeed.layoutManager = layoutManager
        binding.rvMyFeed.adapter = mAdapter
        val scrollListener: RecyclerViewScrollListener = object : RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                Log.d("visible item index", index.toString())
                // play just visible item
                if (index != -1)
                    PlayerViewAdapter.playIndexThenPausePreviousPlayer(index)
            }
        }
        binding.rvMyFeed.addOnScrollListener(scrollListener)
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
        CommonCode(context!!).shareTextWithAnotherApp(context!!, link)
    }

    override fun onAboutCelebratlyClick() {
        val intent = Intent(this.activity, AboutCelebratlyActivity::class.java)
        startActivity(intent)
    }

    private fun initiateLogOut() {
        AuthenticationHelper(this.context!!).saveSignInStatus(1)
        val intent = Intent(this.activity, SignInActivity::class.java)
        startActivity(intent)
    }
}
