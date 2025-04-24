package repository.remote

import model.Post

interface RemoteDataSource {
    fun fetchPostsFrom(url: String): List<Post>
}
