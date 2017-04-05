package me.toptas.rssconvertersample;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.toptas.rssconverter.RssConverterFactory;
import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Fragment for listing fetched {@link RssItem} list
 */
public class RssFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RssItemsAdapter.OnItemClickListener {

    private static final String KEY_FEED = "FEED";

    private String mFeedUrl;
    private RssItemsAdapter mAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swRefresh)
    SwipeRefreshLayout mSwRefresh;

    /**
     * Creates new instance of {@link RssFragment}
     * @param feedUrl Fetched Url Feed
     * @return Fragment
     */
    public static RssFragment newInstance(String feedUrl) {
        RssFragment rssFragment = new RssFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FEED, feedUrl);
        rssFragment.setArguments(bundle);
        return rssFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedUrl = getArguments().getString(KEY_FEED);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new RssItemsAdapter(getActivity());
        mAdapter.setListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mSwRefresh.setOnRefreshListener(this);

        fetchRss();
        return view;
    }

    /**
     * Fetches Rss Feed Url
     */
    private void fetchRss() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(RssConverterFactory.create())
                .build();

        showLoading();
        RssService service = retrofit.create(RssService.class);
        service.getRss(mFeedUrl)
                .enqueue(new Callback<RssFeed>() {
                    @Override
                    public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                        onRssItemsLoaded(response.body().getItems());
                        hideLoading();
                    }

                    @Override
                    public void onFailure(Call<RssFeed> call, Throwable t) {
                        Toast.makeText(getActivity(), "Failed to fetchRss RSS feed!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /**
     * Loads fetched {@link RssItem} list to RecyclerView
     * @param rssItems
     */
    public void onRssItemsLoaded(List<RssItem> rssItems) {
        mAdapter.setItems(rssItems);
        mAdapter.notifyDataSetChanged();
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Shows {@link SwipeRefreshLayout}
     */
    public void showLoading() {
        mSwRefresh.setRefreshing(true);
    }


    /**
     * Hides {@link SwipeRefreshLayout}
     */
    public void hideLoading() {
        mSwRefresh.setRefreshing(false);
    }


    /**
     * Triggers on {@link SwipeRefreshLayout} refresh
     */
    @Override
    public void onRefresh() {
        fetchRss();
    }

    /**
     * Browse {@link RssItem} url
     * @param rssItem
     */
    @Override
    public void onItemSelected(RssItem rssItem) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssItem.getLink()));
        startActivity(intent);
    }

}