package me.toptas.rssconverter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * RSS Feed XML parser
 */
class XMLParser extends DefaultHandler {

    private static final String sEmptyString = "";
    private static final String sItem = "item";
    private static final String sTitle = "title";
    private static final String sMedia = "media";
    private static final String sDescription = "description";
    private static final String sLink = "link";
    private static final String sAtomLink = "atom:link";
    private static final String sUrl = "url";
    private static final String sImage = "image";
    private static final String sPublishDate = "pubdate";

    private boolean mElementOn = false;
    private boolean mParsingTitle = false;
    private boolean mParsingDescription = false;
    private boolean mParsingLink = false;

    private String mElementValue = null;
    private String mTitle = sEmptyString;
    private String mLink;
    private String mImage;
    private String mDate;
    private String mDescription;

    private RssItem mRssItem;
    private final ArrayList<RssItem> mRssItems;

    XMLParser() {
        super();
        mRssItems = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        mElementOn = true;
        switch (localName.toLowerCase()) {
            case sItem:
                mRssItem = new RssItem();
                break;
            case sTitle:
                if (!qName.contains(sMedia)) {
                    mParsingTitle = true;
                    mTitle = sEmptyString;
                }
                break;
            case sDescription:
                mParsingDescription = true;
                mDescription = sEmptyString;
                break;
            case sLink:
                if (!qName.equals(sAtomLink)) {
                    mParsingLink = true;
                    mLink = sEmptyString;
                }
                break;
        }
        /*if (localName.equals(sItem)) {
            mRssItem = new RssItem();
        } else if (localName.equalsIgnoreCase(sTitle) && !qName.contains(sMedia)) {
            mParsingTitle = true;
            mTitle = sEmptyString;
        } else if (localName.equalsIgnoreCase(sDescription)) {
            mParsingDescription = true;
            mDescription = sEmptyString;
        } else if (localName.equalsIgnoreCase(sLink) && !qName.equals(sAtomLink)) {
            mParsingLink = true;
            mLink = sEmptyString;
        }*/
        if (attributes != null) {
            String url = attributes.getValue(sUrl);
            if (url != null && !url.isEmpty()) {
                mImage = url;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        mElementOn = false;
        if (mRssItem != null) {
            switch (localName.toLowerCase()) {
                case sItem:
                    mRssItem = new RssItem();
                    mRssItem.setTitle(mTitle.trim());
                    mRssItem.setLink(mLink);
                    mRssItem.setImage(mImage);
                    mRssItem.setPublishDate(mDate);
                    mRssItem.setDescription((mDescription));
                    if (mImage == null && mDescription != null && getImageSourceFromDescription(mDescription) != null) {
                        mRssItem.setImage(getImageSourceFromDescription(mDescription));
                    }
                    mRssItems.add(mRssItem);
                    mLink = sEmptyString;
                    mImage = null;
                    mDate = sEmptyString;
                    break;
                case sTitle:
                    if (!qName.contains(sMedia)) {
                        mParsingTitle = false;
                        mElementValue = sEmptyString;
                        mTitle = removeNewLine(mTitle);
                    }
                    break;
                case sLink:
                    if (!mElementValue.isEmpty()) {
                        mParsingLink = false;
                        mElementValue = sEmptyString;
                        mLink = removeNewLine(mLink);
                    }
                    break;
                case sImage:
                case sUrl:
                    if (mElementValue != null && !mElementValue.isEmpty()) {
                        mImage = mElementValue;
                    }
                    break;
                case sPublishDate:
                    mDate = mElementValue;
                    break;
                case sDescription:
                    mParsingDescription = false;
                    mElementValue = sEmptyString;
                    break;

            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String buff = new String(ch, start, length);
        if (mElementOn) {
            if (buff.length() > 2) {
                mElementValue = buff;
                mElementOn = false;
            }
        }
        if (mParsingTitle) {
            mTitle = mTitle + buff;
        }
        if (mParsingDescription) {
            mDescription = mDescription + buff;
        }
        if (mParsingLink) {
            mLink = mLink + buff;
        }
    }


    /**
     * Image is parsed from description if image is null
     *
     * @param description description
     * @return Image url
     */
    private String getImageSourceFromDescription(String description) {
        if (description.contains("<img") && description.contains("src")) {
            String[] parts = description.split("src=\"");
            if (parts.length == 2 && parts[1].length() > 0) {
                String src = parts[1].substring(0, parts[1].indexOf("\""));
                String[] srcParts = src.split("http"); // can be removed
                if (srcParts.length > 2) {
                    src = "http" + srcParts[2];
                }
                return src;
            }
        }
        return null;
    }


    private String removeNewLine(String s) {
        if (s == null) {
            return sEmptyString;
        }
        return s.replace("\n", "");
    }

    ArrayList<RssItem> getItems() {
        return mRssItems;
    }
}
