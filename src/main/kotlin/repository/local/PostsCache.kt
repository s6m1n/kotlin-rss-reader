package repository.local

import model.Posts

class PostsCache : LocalDataSource {
    private var latestCache = Posts(emptyList())
    private var prevCache = Posts(emptyList())

    override fun searchPostsBy(keyword: String) = latestCache.filterBy(keyword)

    override fun getNewPosts(): Posts = prevCache.filterNewPosts(latestCache)

    override suspend fun initializeCache(latestCache: Posts) {
        this.latestCache = latestCache
        syncPrevCache()
    }

    override suspend fun refreshCache(latestCache: Posts) {
        syncPrevCache()
        this.latestCache = latestCache
    }

    private fun syncPrevCache() {
        prevCache = latestCache
    }
}
