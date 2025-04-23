package repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import model.Posts

class PostRepository(private val xmlParser: XmlParser) {
    private var latestCache = Posts(emptyList())
    private var prevCache = Posts(emptyList())

    fun searchPostsBy(keyword: String = "") = latestCache.filterBy(keyword).takeLatest(10)

    fun getNewPosts(): Posts = prevCache.filterNewPosts(latestCache)

    suspend fun initializeCache() {
        latestCache = fetchRemotePosts()
        syncPrevCache()
    }

    suspend fun refreshCache() {
        syncPrevCache()
        latestCache = fetchRemotePosts()
    }

    private fun syncPrevCache() {
        prevCache = latestCache
    }

    private suspend fun fetchRemotePosts(): Posts =
        coroutineScope {
            dummyUrls.map {
                async(Dispatchers.IO) {
                    xmlParser.parsePostsFrom(it)
                }
            }.awaitAll().flatten().run { Posts(this) }
        }

    companion object {
        private val dummyUrls =
            listOf(
                "https://rss.blog.naver.com/sh_rew.xml",
                "https://rss.blog.naver.com/salth6.xml",
            )
    }
}
