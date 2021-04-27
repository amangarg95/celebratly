package com.kiprosh.optimizeprime.view.fragment

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentFeedBinding
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedItemBinding
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.helper.RecyclerViewScrollListener
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.FeedsViewModel
import com.kiprosh.optimizeprime.view.activity.MainActivity
import com.kiprosh.optimizeprime.view.adapter.FeedAdapter
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter.Companion.playIndexThenPausePreviousPlayer
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import kotlinx.android.synthetic.main.home_player_control.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.abs

class FeedFragment : Fragment(), FeedAdapter.OnShareClickListener {
    private lateinit var myFeedFragmentBinding: FragmentFeedBinding
    lateinit var apiInterface: APIInterface
    lateinit var progressDialog: ProgressDialog
    private var screenHeight: Int = 0
    private var screenWidth: Int = 0
    private lateinit var feedsViewModel: FeedsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFeedFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        myFeedFragmentBinding.lifecycleOwner = this
        feedsViewModel = ViewModelProviders.of(this).get(FeedsViewModel::class.java)

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
            override fun onItemClick(view: View?, position: Int, model: OccurrencesResponse?, binding: FragmentMyFeedItemBinding) {
                val displayMetrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    screenHeight = displayMetrics.heightPixels
                    screenWidth = displayMetrics.widthPixels
                } else {
                    screenHeight = displayMetrics.widthPixels
                    screenWidth = displayMetrics.heightPixels
                }

                handleFullScreen(binding)
            }
        })
    }

    private fun handleFullScreen(binding: FragmentMyFeedItemBinding) {
        val orientationListener = PlayerOrientationListener(activity)
        orientationListener.enable()

        if (feedsViewModel.isFullScreen) {
            orientationListener.lockPortrait()
            feedsViewModel.isFullScreen = false
            binding.exoPlayerView.exo_full_screen_toggle.setImageDrawable(
                ContextCompat.getDrawable(
                    activity!!,
                    R.drawable.ic_fullscreen_close
                )
            )
            val params: RelativeLayout.LayoutParams =
                binding.exoPlayerView?.layoutParams as RelativeLayout.LayoutParams
            params.width = screenWidth
            params.height = context!!.resources.getDimension(R.dimen.player_height).toInt()
            //params.setMargins(0, 0, 0, 0)
            binding.exoPlayerView?.layoutParams = params

            val layoutParams: ViewGroup.MarginLayoutParams =
                binding.cardPlayer.getLayoutParams() as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(CommonCode(context!!).dpToPx(16), CommonCode(context!!).dpToPx(16),
                CommonCode(context!!).dpToPx(16), CommonCode(context!!).dpToPx(16))
            binding.cardPlayer.requestLayout()

            myFeedFragmentBinding.tvMyFeed.visibility = VISIBLE
            (activity as MainActivity)!!.mainActivityMainBinding.bnvMenu.visibility = VISIBLE

            /*
            Working code
            activity!!.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            activity!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            val params: RelativeLayout.LayoutParams = binding.exoPlayerView.layoutParams as RelativeLayout.LayoutParams
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = LinearLayout.LayoutParams.MATCH_PARENT
            params.setMargins(0, 0, 0, 0)

            binding.exoPlayerView.setLayoutParams(params)
            */
        } else {
            orientationListener.lockLandscape()
            val params: RelativeLayout.LayoutParams =
                binding.exoPlayerView?.layoutParams as RelativeLayout.LayoutParams
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = screenHeight - CommonCode(context!!).getStatusBarHeight()

            val layoutParams: ViewGroup.MarginLayoutParams =
                binding.cardPlayer.getLayoutParams() as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(0, 0, 0, 0)
            binding.cardPlayer.requestLayout()

            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            binding.exoPlayerView?.layoutParams = params
            myFeedFragmentBinding.tvMyFeed.visibility = GONE
            (activity as MainActivity)!!.mainActivityMainBinding.bnvMenu.visibility = GONE

            binding.exoPlayerView.exo_full_screen_toggle.setImageDrawable(
                ContextCompat.getDrawable(
                    activity!!,
                    R.drawable.ic_fullscreen_open
                )
            )
            feedsViewModel.isFullScreen = true
            binding.exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
            /*
            Working code
            activity!!.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            activity!!.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            val params: RelativeLayout.LayoutParams = binding.exoPlayerView.layoutParams as RelativeLayout.LayoutParams
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = LinearLayout.LayoutParams.MATCH_PARENT
            params.setMargins(0, 0, 0, 0)
            binding.exoPlayerView.setLayoutParams(params)
            */
        }
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }

    override fun onShareClick(link: String) {
        //Do Nothing
    }

    inner class PlayerOrientationListener(val activity: Activity?) : OrientationEventListener(
        activity
    ) {

        private val ROT_THRESHOLD = 5

        private val ROT_0 = 0
        private val ROT_90 = 90
        private val ROT_180 = 180
        private val ROT_270 = 270

        private var orientationLockedPortrait = false
        private var orientationLockedLandscape = false

        fun lockLandscape() {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            orientationLockedLandscape = true
            orientationLockedPortrait = false
        }

        fun lockPortrait() {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            orientationLockedPortrait = true
            orientationLockedLandscape = false
        }

        override fun onOrientationChanged(orientation: Int) {
            if (orientation == ORIENTATION_UNKNOWN) {
                return
            }

            val rotation: Int =
                when {
                    abs(orientation - ROT_0) < ROT_THRESHOLD -> ROT_0
                    abs(orientation - ROT_90) < ROT_THRESHOLD -> ROT_90
                    abs(orientation - ROT_180) < ROT_THRESHOLD -> ROT_180
                    abs(orientation - ROT_270) < ROT_THRESHOLD -> ROT_270
                    else -> ORIENTATION_UNKNOWN
                }
            when (rotation) {
                ROT_0 -> if (!orientationLockedLandscape) {
                    if (!feedsViewModel.isFullScreen) {
                        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        orientationLockedPortrait = false
                    }
                }
                ROT_90 -> if (!orientationLockedPortrait) {
                    activity?.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    orientationLockedLandscape = false
                }
                ROT_180 -> if (!orientationLockedLandscape) {
                    activity?.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                    orientationLockedPortrait = false
                }
                ROT_270 -> if (!orientationLockedPortrait) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    orientationLockedLandscape = false
                }
            }
        }
    }
}