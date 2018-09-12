package me.toptas.rssconverter

/**
 * RSS Feed response model
 */

data class RssFeed(var items: List<RssItem>? = null)