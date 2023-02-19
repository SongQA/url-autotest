package url

import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val links = extractLinksFromHtml("https://www.naver.com")
    links.forEach {
        val responseCode = getResponseCode(it)
        println("Response code $it: $responseCode")
    }
}

fun extractLinksFromHtml(urlString: String): List<String> {
    val doc = Jsoup.connect(urlString).get()
    val linkElements = doc.select("a[href], img[src], link[href]")
    val links = linkElements.mapNotNull {
        when {
            it.tagName() == "a" -> it.absUrl("href")
            it.tagName() == "img" -> it.absUrl("src")
            it.tagName() == "link" -> it.absUrl("href")
            else -> null
        }
    }

    return links.toSet().toList()
}

fun getResponseCode(urlString: String): Int {
    return if (urlString.startsWith("http") || urlString.startsWith("https")) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connect()

        val responseCode = connection.responseCode

        connection.disconnect()

        responseCode
    } else {
        0
    }
}
