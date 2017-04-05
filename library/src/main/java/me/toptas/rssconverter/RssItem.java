package me.toptas.rssconverter;

import java.io.Serializable;

/**
 * Model for Rss Item
 */
public class RssItem implements Serializable {

    private String mTitle;
    private String mLink;
    private String mImage;
    private String mPublishDate;
    private String mDescription;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title.replace("&#39;", "'").replace("&#039;", "'");
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link.trim();
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public void setPublishDate(String publishDate) {
        this.mPublishDate = publishDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (mTitle != null) {
            builder.append(mTitle).append("\n");
        }
        if (mLink != null) {
            builder.append(mLink).append("\n");
        }
        if (mImage != null) {
            builder.append(mImage).append("\n");
        }
        if (mDescription != null) {
            builder.append(mDescription);
        }
        return builder.toString();
    }
}
