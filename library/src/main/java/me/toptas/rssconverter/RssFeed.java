package me.toptas.rssconverter;

import java.util.List;

/**
 * RSS Feed response model
 */

public class RssFeed {

    /**
     * List of parsed {@link RssItem} objects
     */
    private List<RssItem> mItems;

    public List<RssItem> getItems() {
        return mItems;
    }

    void setItems(List<RssItem> items) {
        mItems = items;
    }
}
