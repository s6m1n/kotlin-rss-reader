package repository

import model.Posts

interface PostRepository {
    fun searchPostsBy(keyword: String = ""): Posts

    fun getNewPosts(): Posts

    suspend fun initialize()

    suspend fun refresh()
}
