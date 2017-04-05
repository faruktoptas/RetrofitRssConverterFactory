package me.toptas.rssconverter;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class RssResponseBodyConverter<T> implements Converter<ResponseBody, RssFeed> {


    RssResponseBodyConverter() {
    }

    @Override
    public RssFeed convert(ResponseBody value) {
        RssFeed rssFeed = new RssFeed();
        try {
            XMLParser parser = new XMLParser();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(parser);
            InputSource inputSource = new InputSource(value.charStream());
            xmlReader.parse(inputSource);
            ArrayList<RssItem> items = parser.getItems();
            rssFeed.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rssFeed;
    }

}