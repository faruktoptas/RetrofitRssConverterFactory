package me.toptas.rssconverter

import java.lang.reflect.Type

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

/**
 * A [converter][Converter.Factory] which uses [XMLParser] to parse RSS feeds.
 */

class RssConverterFactory
/**
 * Constructor
 */
private constructor() : Converter.Factory() {

    override fun responseBodyConverter(type: Type?,
                                       annotations: Array<Annotation>?,
                                       retrofit: Retrofit?):
            Converter<ResponseBody, *> = RssResponseBodyConverter()

    companion object {

        /**
         * Creates an instance
         *
         * @return instance
         */
        fun create(): RssConverterFactory {
            return RssConverterFactory()
        }
    }
}
