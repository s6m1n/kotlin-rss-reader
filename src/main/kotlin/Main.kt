import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

val dummyUrls =
    listOf(
        "https://rss.blog.naver.com/sh_rew.xml",
        "https://techblog.woowahan.com/feed/",
        "https://rss.blog.naver.com/rlaalsdn456456.xml",
    )

fun main() {
    var keyword = ""
    while (true) {
        print("검색어를 입력하세요 (없으면 전체 출력, 종료는 q): ")
        keyword = readln()
        if (keyword == "q") break
        readPosts(keyword)
    }
}

private fun readPosts(keyword: String) =
    runBlocking {
        dummyUrls.forEachIndexed { idx, url ->
            launch {
                val result = getPostsWithContext(url, keyword)
                printPosts(result.posts)
            }
        }
    }

private suspend fun getPostsWithContext(
    url: String,
    keyword: String,
): Posts =
    withContext(Dispatchers.IO) {
        getPostsBy(url, keyword)
    }

private fun getPostsBy(
    url: String,
    keyword: String = "",
): Posts {
    val nodeList = getNodesBy(url)
    val result = createParsedPost(nodeList, keyword)
    return Posts(result).sorted()
}

private fun getNodesBy(url: String): NodeList {
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val doc = builder.parse(url)
    val nodeList: NodeList = doc.getElementsByTagName("item")
    return nodeList
}

private fun createParsedPost(
    nodeList: NodeList,
    keyword: String,
): List<Post> {
    return if (keyword == "") getAllPosts(nodeList) else getPostsBy(nodeList, keyword)
}

private fun getPostsBy(
    nodeList: NodeList,
    keyword: String,
): List<Post> {
    val result = mutableListOf<Post>()
    for (i in 0 until (nodeList.length).coerceAtMost(10)) {
        val element = nodeList.item(i) as Element
        val title = element.getElementsByTagName("title").item(0).textContent
        if (title.contains(keyword)) {
            val link = element.getElementsByTagName("link").item(0).textContent
            val date = element.getElementsByTagName("pubDate").item(0).textContent.parseRssPubDate()
            result.add(Post(i + 1, title, link, date))
        }
    }
    return result.toList()
}

private fun getAllPosts(nodeList: NodeList): List<Post> {
    val result = mutableListOf<Post>()
    for (i in 0 until (nodeList.length).coerceAtMost(10)) {
        val element = nodeList.item(i) as Element
        val title = element.getElementsByTagName("title").item(0).textContent
        val link = element.getElementsByTagName("link").item(0).textContent
        val date = element.getElementsByTagName("pubDate").item(0).textContent.parseRssPubDate()
        result.add(Post(i + 1, title, link, date))
    }
    return result.toList()
}

fun printPosts(result: List<Post>) {
    result.forEachIndexed { index, post ->
        println(post)
    }
}
