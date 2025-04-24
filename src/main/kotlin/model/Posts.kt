package model

data class Posts(val posts: List<Post>) {
    operator fun plus(postPosts: Posts) = Posts(this.posts + postPosts.posts)

    fun isNotEmpty() = posts.isNotEmpty()

    fun filterNewPosts(newPosts: Posts) = Posts(newPosts.posts.filter { it !in posts })

    fun filterBy(keyWord: String) = Posts(posts.filter { it.title.contains(keyWord) }).takeNewest(10)

    private fun takeNewest(number: Int) = Posts(posts.sortedByDescending { it.date }.take(number))
}
