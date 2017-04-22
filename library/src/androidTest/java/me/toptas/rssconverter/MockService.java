package me.toptas.rssconverter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import retrofit2.Call;
import retrofit2.http.Url;
import retrofit2.mock.BehaviorDelegate;

public class MockService implements RssService {

    private final BehaviorDelegate<RssService> delegate;
    private final String mRssFeedResponse;

    MockService(BehaviorDelegate<RssService> service, String rssFeedResponse) {
        this.delegate = service;
        mRssFeedResponse = rssFeedResponse;
    }

    @Override
    public Call<RssFeed> getRss(@Url String url) {
        RssFeed rssFeed = new RssFeed();
        try {
            XMLParser myXMLHandler = new XMLParser();
            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();
            xmlR.setContentHandler(myXMLHandler);
            InputSource inputSource = new InputSource(new StringReader(mRssFeedResponse));
            xmlR.parse(inputSource);
            ArrayList<RssItem> items = myXMLHandler.getItems();
            rssFeed.setItems(items);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return delegate.returningResponse(rssFeed).getRss(url);
    }
}