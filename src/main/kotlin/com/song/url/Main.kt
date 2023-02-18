package url

import java.net.HttpURLConnection
import java.net.URL
import org.jsoup.Jsoup

fun main() {

    val links = extractLinksFromHtml("https://www.naver.com")
    links.forEach {
        val responseCode = getResponseCode(it)
        println("Response code $it: $responseCode")
    }
    }

    fun extractLinksFromHtml(urlString: String): List<String> {
        val doc = Jsoup.connect(urlString).get()
        val linkElements = doc.select("a[href], img[src]")
        val links = linkElements.mapNotNull {
            when {
                it.tagName() == "a" -> it.absUrl("href")
                it.tagName() == "img" -> it.absUrl("src")
                else -> null
            }
        }
        return links
    }

    fun getResponseCode(urlString: String): Int {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connect()

        val responseCode = connection.responseCode

        connection.disconnect()

        return responseCode
    }
