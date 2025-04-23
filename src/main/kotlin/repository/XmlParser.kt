package repository

import model.Post
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser {
    fun parsePostsFrom(url: String): List<Post> {
        val nodeList = extractNodesFrom(url)
        val result = parsePostsFrom(nodeList)
        return result
    }

    private fun extractNodesFrom(url: String): NodeList {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(url)
        val nodeList: NodeList = doc.getElementsByTagName("item")
        return nodeList
    }

    private fun parsePostsFrom(nodeList: NodeList): List<Post> {
        val result = mutableListOf<Post>()
        for (i in 0 until nodeList.length) {
            val element = nodeList.item(i) as Element
            val title = element.getElementsByTagName("title").item(0).textContent
            val link = element.getElementsByTagName("link").item(0).textContent
            val date = element.getElementsByTagName("pubDate").item(0).textContent.parseRssPubDate()
            result.add(Post(title, link, date))
        }
        return result.toList()
    }

    private fun String.parseRssPubDate(): LocalDate {
        val inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        return ZonedDateTime.parse(this, inputFormatter).toLocalDate()
    }
}
