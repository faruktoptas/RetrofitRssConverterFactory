package me.toptas.rssconvertersample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.toptas.rssconverter.RssItem;

/**
 * Adapter for listing {@link RssItem}
 */
class RssItemsAdapter extends RecyclerView.Adapter<RssItemsAdapter.ViewHolder> {

    private final List<RssItem> mItems = new ArrayList<>();
    private OnItemClickListener mListener;
    private final Context mContext;

    RssItemsAdapter(Context context) {
        mContext = context;
    }

    /**
     * Item click listener
     *
     * @param listener listener
     */
    void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * Set {@link RssItem} list
     *
     * @param items item list
     */
    void setItems(List<RssItem> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    @Override
    public RssItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rss_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RssItemsAdapter.ViewHolder holder, int position) {
        if (mItems.size() <= position) {
            return;
        }
        final RssItem item = mItems.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textPubDate.setText(item.getPublishDate());
        if (item.getEnclosures().size() > 0) {
            holder.textEnclosure.setText(item.getEnclosures().get(0).getLink());
        } else {
            holder.textEnclosure.setVisibility(View.GONE);
        }

        if (item.getImage() != null) {
            Picasso.with(mContext).load(item.getImage()).
                    fit()
                    .centerCrop()
                    .into(holder.imageThumb);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) mListener.onItemSelected(item);
            }
        });
        holder.itemView.setTag(item);

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    interface OnItemClickListener {
        void onItemSelected(RssItem rssItem);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView textTitle;

        @BindView(R.id.tvPubDate)
        TextView textPubDate;

        @BindView(R.id.tvEnclosure)
        TextView textEnclosure;

        @BindView(R.id.ivThumb)
        ImageView imageThumb;

        @BindView(R.id.llTextContainer)
        RelativeLayout llTextContainer;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
