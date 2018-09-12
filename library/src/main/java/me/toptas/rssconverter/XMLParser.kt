package me.toptas.rssconverter

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

/**
 * RSS Feed XML parser
 */
internal class XMLParser : DefaultHandler() {

    private var elementOn = false
    private var parsingTitle = false
    private var parsingDescription = false
    private var parsingLink = false

    private var elementValue: String? = null
    private var title = EMPTY_STRING
    private var link: String? = null
    private var image: String? = null
    private var date: String? = null
    private var description: String? = null

    private var rssItem: RssItem? = null
    val items = arrayListOf<RssItem>()


    @Throws(SAXException::class)
    override fun startElement(uri: String, localName: String, qName: String,
                              attributes: Attributes?) {
        elementOn = true
        when (localName.toLowerCase()) {
            ITEM -> rssItem = RssItem()
            TITLE -> if (!qName.contains(MEDIA)) {
                parsingTitle = true
                title = EMPTY_STRING
            }
            DESCRIPTION -> {
                parsingDescription = true
                description = EMPTY_STRING
            }
            LINK -> if (qName != ATOM_LINK) {
                parsingLink = true
                link = EMPTY_STRING
            }
        }

        if (attributes != null) {
            val url = attributes.getValue(URL)
            if (url != null && !url.isEmpty()) {
                image = url
            }
        }
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        elementOn = false
        if (rssItem != null) {
            when (localName.toLowerCase()) {
                ITEM -> {
                    rssItem = RssItem()
                    rssItem?.let {
                        it.title = title.trim { it <= ' ' }
                        it.link = link
                        it.image = image
                        it.publishDate = date
                        it.description = description
                        if (image == null && description != null && getImageSourceFromDescription(description) != null) {
                            it.image = getImageSourceFromDescription(description!!)
                        }
                        items.add(it)
                    }
                    link = EMPTY_STRING
                    image = null
                    date = EMPTY_STRING
                }
                TITLE -> if (!qName.contains(MEDIA)) {
                    parsingTitle = false
                    elementValue = EMPTY_STRING
                    title = removeNewLine(title)
                }
                LINK -> if (elementValue?.isNotEmpty() == true) {
                    parsingLink = false
                    elementValue = EMPTY_STRING
                    link = removeNewLine(link)
                }
                IMAGE, URL -> if (elementValue != null && elementValue?.isNotEmpty() == true) {
                    image = elementValue
                }
                PUBLISH_DATE -> date = elementValue
                DESCRIPTION -> {
                    parsingDescription = false
                    elementValue = EMPTY_STRING
                }
            }
        }
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        val buff = String(ch, start, length)
        if (elementOn) {
            if (buff.length > 2) {
                elementValue = buff
                elementOn = false
            }
        }
        if (parsingTitle) {
            title += buff
        }
        if (parsingDescription) {
            description = description!! + buff
        }
        if (parsingLink) {
            link = link!! + buff
        }
    }


    /**
     * Image is parsed from description if image is null
     *
     * @param description description
     * @return Image url
     */
    private fun getImageSourceFromDescription(description: String?): String? {
        if (description?.contains("<img") == true && description.contains("src")) {
            val parts = description.split("src=\"".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            if (parts.size == 2 && parts[1].isNotEmpty()) {
                var src = parts[1].substring(0, parts[1].indexOf("\""))
                val srcParts = src.split("http".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray() // can be removed
                if (srcParts.size > 2) {
                    src = "http" + srcParts[2]
                }
                return src
            }
        }
        return null
    }


    private fun removeNewLine(s: String?): String {
        return s?.replace("\n", "") ?: EMPTY_STRING
    }

    companion object {

        private const val EMPTY_STRING = ""
        private const val ITEM = "item"
        private const val TITLE = "title"
        private const val MEDIA = "media"
        private const val DESCRIPTION = "description"
        private const val LINK = "link"
        private const val ATOM_LINK = "atom:link"
        private const val URL = "url"
        private const val IMAGE = "image"
        private const val PUBLISH_DATE = "pubdate"
    }
}
