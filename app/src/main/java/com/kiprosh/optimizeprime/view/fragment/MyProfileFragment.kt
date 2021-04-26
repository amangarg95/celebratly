package com.kiprosh.optimizeprime.view.fragment

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyProfileBinding
import com.kiprosh.optimizeprime.dummy.DummyContent
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.MyProfileAdapter
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

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

                binding.rvMyFeed.adapter = MyProfileAdapter(this@MyProfileFragment, context!!, user, recyclerDataArrayList, false)
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
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.yellow)
    }

    override fun onToggleClick(isFullScreen: Boolean) {

    }
}