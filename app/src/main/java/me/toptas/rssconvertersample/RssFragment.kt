package me.toptas.rssconvertersample


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import kotlinx.android.synthetic.main.fragment_rss.*
import me.toptas.rssconverter.RssConverterFactory
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


/**
 * Fragment for listing fetched [RssItem] list
 */
class RssFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, RssItemsAdapter.OnItemClickListener {

    private var feedUrl: String? = null
    private lateinit var mAdapter: RssItemsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedUrl = arguments?.getString(KEY_FEED)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rss, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = RssItemsAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        swRefresh.setOnRefreshListener(this)

        fetchRss()
    }

    /**
     * Fetches Rss Feed Url
     */
    private fun fetchRss() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(RssConverterFactory.create())
                .build()

        showLoading()
        val service = retrofit.create(RssService::class.java)

        feedUrl?.apply {
            service.getRss(this)
                    .enqueue(object : Callback<RssFeed> {
                        override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                            onRssItemsLoaded(response.body()!!.items!!)
                            hideLoading()
                        }

                        override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                            Toast.makeText(activity, "Failed to fetchRss RSS feed!", Toast.LENGTH_SHORT).show()

                        }
                    })
        }
    }

    /**
     * Loads fetched [RssItem] list to RecyclerView
     * @param rssItems
     */
    fun onRssItemsLoaded(rssItems: List<RssItem>) {
        mAdapter.setItems(rssItems)
        mAdapter.notifyDataSetChanged()
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
        }
    }

    /**
     * Shows [SwipeRefreshLayout]
     */
    private fun showLoading() {
        swRefresh.isRefreshing = true
    }


    /**
     * Hides [SwipeRefreshLayout]
     */
    fun hideLoading() {
        swRefresh.isRefreshing = false
    }


    /**
     * Triggers on [SwipeRefreshLayout] refresh
     */
    override fun onRefresh() {
        fetchRss()
    }

    /**
     * Browse [RssItem] url
     * @param rssItem
     */
    override fun onItemSelected(rssItem: RssItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rssItem.link))
        startActivity(intent)
    }

    companion object {
        private const val KEY_FEED = "FEED"

        /**
         * Creates new instance of [RssFragment]
         * @param feedUrl Fetched Url Feed
         * @return Fragment
         */
        fun newInstance(feedUrl: String): RssFragment {
            val rssFragment = RssFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_FEED, feedUrl)
            rssFragment.arguments = bundle
            return rssFragment
        }
    }

}