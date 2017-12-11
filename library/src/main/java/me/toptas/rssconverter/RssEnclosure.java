package me.toptas.rssconverter;

import java.io.Serializable;

/**
 * Model for Rss Item
 */
public class RssEnclosure implements Serializable {
    private String mType;
    private String mLink;
    private String mLength;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type.replace("&#39;", "'").replace("&#039;", "'");
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link.trim();
    }

    public String getLength() {
        return mLength;
    }

    public void setLength(String length) {
        this.mLength = length.trim();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (mType != null) {
            builder.append(mType).append("\n");
        }
        if (mLink != null) {
            builder.append(mLink).append("\n");
        }
        if (mLength != null) {
            builder.append(mLength).append("\n");
        }
        return builder.toString();
    }
}
