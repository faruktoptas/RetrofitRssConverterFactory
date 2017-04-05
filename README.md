# RetrofitRssConverterFactory
A Retrofit2 converter which parses Rss feeds.

## Sample Usage
```java
public interface RssService {
    /**
     * No baseUrl defined. Each RSS Feed will be consumed by it's Url
     * @param url RSS Feed Url
     * @return Retrofit Call
     */
    @GET
    Call<RssFeed> getRss(@Url String url);
}
```

```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://github.com")
        .addConverterFactory(RssConverterFactory.create())
        .build();

RssService service = retrofit.create(RssService.class);
service.getRss("RSS_FEED_URL")
        .enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                // Populate list with response.body().getItems()
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                // Show failure message
            }
        });
```

## Contribute
You can contribute by opening a pull request to **dev** branch.
Please try to push one feature in one commit for a clean commit history.

License
=======

    Copyright 2017 Faruk Toptaş

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.