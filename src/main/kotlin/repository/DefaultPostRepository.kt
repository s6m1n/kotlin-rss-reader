package repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import model.Posts
import repository.local.LocalDataSource
import repository.remote.RemoteDataSource

class DefaultPostRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : PostRepository {
    override fun searchPostsBy(keyword: String) = localDataSource.searchPostsBy(keyword)

    override fun getNewPosts(): Posts = localDataSource.getNewPosts()

    override suspend fun initialize() {
        val initialPosts = fetchRemotePosts()
        localDataSource.initializeCache(initialPosts)
    }

    override suspend fun refresh() {
        val initialPosts = fetchRemotePosts()
        localDataSource.initializeCache(initialPosts)
    }

    private suspend fun fetchRemotePosts(): Posts =
        coroutineScope {
            dummyUrls.map {
                async(Dispatchers.IO) {
                    try {
                        remoteDataSource.fetchPostsFrom(it)
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
            }.awaitAll().flatten().run { Posts(this) }
        }

    companion object {
        private val dummyUrls =
            listOf(
                "https:// 말도 안되는 URL",
                "https://techblog.woowahan.com/feed/",
                "https://aws.amazon.com/ko/blogs/tech/feed/",
                "https://toss.tech/rss.xml",
                "https:// 예외를 만드는 URL",
                "https://helloworld.kurly.com/feed.xml",
                "https://developers.hyundaimotorgroup.com/blog/rss",
                "https://engineering.linecorp.com/ko/feed/index.html",
            )
    }
}
