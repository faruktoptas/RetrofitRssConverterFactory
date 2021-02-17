# Retrofit2 Rss Converter Factory [![Status](https://travis-ci.org/faruktoptas/RetrofitRssConverterFactory.svg?branch=master)](https://travis-ci.org/faruktoptas/RetrofitRssConverterFactory) [![](https://jitpack.io/v/faruktoptas/RetrofitRssConverterFactory.svg)](https://jitpack.io/#faruktoptas/RetrofitRssConverterFactory) [![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=10)

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
    implementation 'com.github.faruktoptas:RetrofitRssConverterFactory:0.1.0'
}
```


## Sample Usage
```kotlin
interface RssService {
    @GET
    fun getRss(@Url url: String): Call<RssFeed>
}
```

```java
val retrofit = Retrofit.Builder()
        .baseUrl("https://github.com")
        .addConverterFactory(RssConverterFactory.create())
        .build()

val service = retrofit.create(RssService::class.java)
service.getRss("RSS_FEED_URL")
        .enqueue(object : Callback<RssFeed> {
            override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                // Populate list with response.body().getItems()
            }

            override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                // Show failure message
            }
        })
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
