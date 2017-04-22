package me.toptas.rssconverter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by ftoptas on 21/04/17.
 */

interface RssService {
    @GET
    Call<RssFeed> getRss(@Url String url);
}
