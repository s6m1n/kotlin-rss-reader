data class Post(
    val index: Int,
    val title: String,
    val link: String,
    val date: String,
) {
    override fun toString() = "[$index] $title ($date) - $link"
}

data class Posts(val posts: List<Post>) {
    operator fun Posts.plus(postPosts: Posts): Posts {
        return Posts(this.posts + postPosts.posts)
    }

    fun sorted() = Posts(posts.sortedBy { it.date })
}
