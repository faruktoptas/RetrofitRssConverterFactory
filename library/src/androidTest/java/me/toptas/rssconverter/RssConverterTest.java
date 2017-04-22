package me.toptas.rssconverter;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RssConverterTest {

    private BehaviorDelegate<RssService> mDelegate;
    private Context mContext;

    @Before
    public void setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(RssConverterFactory.create())
                .baseUrl("http://github.com")
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();
        mDelegate = mockRetrofit.create(RssService.class);

        mContext = InstrumentationRegistry.getContext();


    }

    @LargeTest
    @Test
    public void test1() throws Exception {
        testFolder("1");
    }

    @LargeTest
    @Test
    public void test2() throws Exception {
        testFolder("2");
    }

    @LargeTest
    @Test
    public void test3() throws Exception {
        testFolder("3");
    }

    @LargeTest
    @Test
    public void test4() throws Exception {
        testFolder("4");
    }

    @LargeTest
    @Test
    public void test5() throws Exception {
        testFolder("5");
    }

    @LargeTest
    @Test
    public void test6() throws Exception {
        testFolder("6");
    }

    @LargeTest
    @Test
    public void test7() throws Exception {
        testFolder("7");
    }

    @LargeTest
    @Test
    public void test8() throws Exception {
        testFolder("8");
    }

    @LargeTest
    @Test
    public void test9() throws Exception {
        testFolder("9");
    }

    @LargeTest
    @Test
    public void test10() throws Exception {
        testFolder("10");
    }

    @LargeTest
    @Test
    public void test11() throws Exception {
        testFolder("11");
    }


    private void testFolder(String folder) throws Exception {
        String path = "rss/" + folder;
        String[] files = mContext.getAssets().list(path);
        try {
            for (String file : files) {
                MockService mMockService = new MockService(mDelegate,
                        readFromAssets(mContext, path + "/" + file));
                Log.v("asd", "Testing: " + file);

                Call<RssFeed> feedCall = mMockService.getRss("");
                Response<RssFeed> quoteOfTheDayResponse = feedCall.execute();

                Assert.assertTrue(quoteOfTheDayResponse.isSuccessful());
                Assert.assertEquals(true, quoteOfTheDayResponse.body().getItems().get(0).getTitle().length() > 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromAssets(Context context, String fileName) {
        String ret = null;
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName)));
            String mLine;
            StringBuilder builder = new StringBuilder();
            while ((mLine = reader.readLine()) != null) {
                builder.append(mLine);
            }
            ret = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
