# Retrofit2 Rss Converter Factory [![Status](https://travis-ci.org/faruktoptas/RetrofitRssConverterFactory.svg?branch=master)](https://travis-ci.org/faruktoptas/RetrofitRssConverterFactory) [![](https://jitpack.io/v/faruktoptas/RetrofitRssConverterFactory.svg)](https://jitpack.io/#faruktoptas/RetrofitRssConverterFactory) [![API](https://img.shields.io/badge/API-10%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=10)

A Retrofit2 converter which parses Rss feeds.

# Gradle Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.faruktoptas:RetrofitRssConverterFactory:0.0.3'
}
```


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
