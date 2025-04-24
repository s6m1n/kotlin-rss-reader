package view

import model.Post
import model.Posts

object DefaultPostView : PostView {
    override fun printLoadingMessage() {
        println("[Loading] 포스트를 불러오고 있습니다. . .")
    }

    override fun printQuitProgramMessage() {
        println("프로그램을 종료합니다.")
    }

    override fun printExceptionMessage() {
        println("프로그램이 예기치 않게 종료되었습니다.")
    }

    override fun readKeyword(): String {
        print("검색어를 입력하세요 (없으면 전체 출력, 종료는 ${QUIT_COMMAND}): ")
        return readln()
    }

    override fun printIndexedPosts(posts: Posts) {
        posts.posts.forEachIndexed { index, post ->
            println("[${index + 1}] ${post.toDisplayString()}")
        }
        println("\n")
    }

    override fun printNewPosts(posts: Posts) {
        println("\n")
        posts.posts.forEach { post ->
            println("[new] ${post.toDisplayString()}")
        }
        println("\n")
    }

    private fun Post.toDisplayString() = "$title ($date) - $link"

    const val QUIT_COMMAND = "q"
}
