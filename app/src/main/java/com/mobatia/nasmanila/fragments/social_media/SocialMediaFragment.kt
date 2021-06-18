package com.mobatia.nasmanila.fragments.social_media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobatia.nasmanila.R


class SocialMediaFragment(s: String, tabSocialMedia: Any) : Fragment() {

//    var mTitleTextView: TextView? = null
//    private var mRootView: View? = null
//    private var mContext: Context? = null
//    private val mAboutUsList: ListView? = null
//    private val mTitle: String? = null
//    private val mTabId: String? = null
//    private val mProgressDialog: RelativeLayout? = null
//    private val mBannerImage: ImageView? = null
//    private var facebook: ImageView? = null
//    private  var twitter:android.widget.ImageView? = null
//    private  var instagram:android.widget.ImageView? = null
//    var type: String? = null
//    private val mSocialMediaArraylistFacebook = ArrayList<SocialMediaModel>()
//    private val mSocialMediaArraylistTwitter = ArrayList<SocialMediaModel>()
//    private val mSocialMediaArraylistInstagram = ArrayList<SocialMediaModel>()
//    private val mSocialMediaArray = ArrayList<SocialMediaModel>()
//
//    var bannerImagePager: ImageView? = null
//    var bannerUrlImageArray: ArrayList<String>? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        mRootView = inflater.inflate(
//            R.layout.fragment_social_media, container,
//            false
//        )
//
//        mContext = activity
//
//        initialiseUI()
//        if (appUtils.checkInternet(mContext!!)) {
//            callSocialMediaListAPI(URLConstants.URL_GET_SOCIALMEDIA_LIS)
//        } else {
//            appUtils.showDialogAlertDismiss(
//                mContext as Activity?,
//                "Network Error",
//                getString(R.string.no_internet),
//                R.drawable.nonetworkicon,
//                R.drawable.roundred
//            )
//        }
//        return mRootView
//    }
//
//    private fun callSocialMediaListAPI(url: String) {
//        val call: Call<ResponseBody> = ApiClient.getApiService().aboutUsListCall(preferenceManager.getAccessToken(mContext))
//        call.enqueue(object : Callback<ResponseBody> {})
//    }
//
//    private fun initialiseUI() {
//        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
//        mTitleTextView!!.text = NaisClassNameConstants.SOCIAL_MEDIA
//        facebook = mRootView!!.findViewById<View>(R.id.facebookButton) as ImageView
//        twitter = mRootView!!.findViewById<View>(R.id.twitterButton) as ImageView
//        instagram = mRootView!!.findViewById<View>(R.id.instagramButton) as ImageView
//        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImageViewPager) as ImageView
//
//        facebook!!.setOnClickListener {
//            if (mSocialMediaArraylistFacebook.size <= 0) {
//                appUtils.showDialogAlertDismiss(
//                    mContext as Activity?,
//                    mContext!!.getString(R.string.alert_heading),
//                    "Link not available.",
//                    R.drawable.exclamationicon,
//                    R.drawable.round
//                )
//            } else if (mSocialMediaArraylistFacebook.size == 1) {
//
//                val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
//                intent.putExtra("url", mSocialMediaArraylistFacebook[0].url)
//                startActivity(intent)
//            } else {
//                showSocialMediaList(mSocialMediaArraylistFacebook, type)
//            }
//        }
//        twitter!!.setOnClickListener {
//            if (mSocialMediaArraylistTwitter.size == 1) {
//                val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
//                intent.putExtra("url", mSocialMediaArraylistTwitter[0].url)
//                startActivity(intent)
//            } else {
//                showSocialMediaList(mSocialMediaArraylistTwitter, type)
//            }
//
//        }
//        instagram!!.setOnClickListener {
//            if (mSocialMediaArraylistInstagram.size <= 0) {
//                appUtils.showDialogAlertDismiss(
//                    mContext as Activity?,
//                    mContext!!.getString(R.string.alert_heading),
//                    "Link not available.",
//                    R.drawable.exclamationicon,
//                    R.drawable.round
//                )
//            } else if (mSocialMediaArraylistInstagram.size == 1) {
//                val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
//                intent.putExtra("url", mSocialMediaArraylistInstagram[0].url)
//                startActivity(intent)
//            } else {
//                showSocialMediaList(mSocialMediaArraylistInstagram, type)
//            }
//        }
//    }
//
//    private fun showSocialMediaList(mSocialMediaArray: ArrayList<SocialMediaModel>, type: String?) {
//        val dialog = Dialog(mContext!!)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dialog_social_media_list)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val dialogDismiss = dialog.findViewById<View>(R.id.btn_dismiss) as Button
//        val iconImageView = dialog.findViewById<View>(R.id.iconImageView) as ImageView
//        val socialMediaList = dialog.findViewById<View>(R.id.recycler_view_social_media) as RecyclerView
//
//        //if(mSocialMediaArray.get())
//        if (type == "facebook") {
//            iconImageView.setImageResource(R.drawable.facebookiconmedia)
//            val sdk = Build.VERSION.SDK_INT
//            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                iconImageView.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundfb))
//                dialogDismiss.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.buttonfb))
//            } else {
//                iconImageView.background = mContext!!.resources.getDrawable(R.drawable.roundfb)
//                dialogDismiss.background = mContext!!.resources.getDrawable(R.drawable.buttonfb)
//            }
//        } else if (type == "twitter") {
//            iconImageView.setImageResource(R.drawable.twittericon)
//            val sdk = Build.VERSION.SDK_INT
//            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                iconImageView.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundtw))
//                dialogDismiss.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.buttontwi))
//            } else {
//                iconImageView.background = mContext!!.resources.getDrawable(R.drawable.roundtw)
//                dialogDismiss.background = mContext!!.resources.getDrawable(R.drawable.buttontwi)
//            }
//        } else {
//            iconImageView.setImageResource(R.drawable.instagramicon)
//            val sdk = Build.VERSION.SDK_INT
//            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//                iconImageView.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.roundins))
//                dialogDismiss.setBackgroundDrawable(mContext!!.resources.getDrawable(R.drawable.buttonins))
//            } else {
//                iconImageView.background = mContext!!.resources.getDrawable(R.drawable.roundins)
//                dialogDismiss.background = mContext!!.resources.getDrawable(R.drawable.buttonins)
//            }
//        }
//        socialMediaList.addItemDecoration(DividerItemDecoration(mContext!!.resources.getDrawable(R.drawable.list_divider)))
//
//        socialMediaList.setHasFixedSize(true)
//        val llm = LinearLayoutManager(mContext)
//        llm.orientation = LinearLayoutManager.VERTICAL
//        socialMediaList.layoutManager = llm
//
//        val socialMediaAdapter = SocialMediaAdapter(mContext!!, mSocialMediaArray)
//        socialMediaList.adapter = socialMediaAdapter
//        dialogDismiss.setOnClickListener {
//            dialog.dismiss()
//        }
//        socialMediaList.addOnItemClickListener(object : OnItemClickListener {
//            override fun onItemClicked(position: Int, view: View) {
//                if (type == "facebook") {
//                    val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
//                    intent.putExtra("url", mSocialMediaArraylistFacebook[position].url)
//                    startActivity(intent)
//                } else if (type == "twitter") {
//                    val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
//                    intent.putExtra("url", mSocialMediaArraylistTwitter[position].url)
//                    startActivity(intent)
//                } else if (type == "instagram") {
//                    val intent = Intent(mContext, FullscreenWebViewActivityNoHeader::class.java)
//                    intent.putExtra("url", mSocialMediaArraylistInstagram[position].url)
//                    startActivity(intent)
//                }
//            }
//
//        })
//        dialog.show()
//    }



}