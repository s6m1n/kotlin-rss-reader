package repository.local
import model.Posts

interface LocalDataSource {
    fun searchPostsBy(keyword: String): Posts

    fun getNewPosts(): Posts

    suspend fun initializeCache(latestCache: Posts)

    suspend fun refreshCache(latestCache: Posts)
}
