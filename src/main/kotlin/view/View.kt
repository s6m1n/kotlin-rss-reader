package view

import model.Posts
import java.lang.Thread.currentThread

class View {
    fun readKeyword(): String {
        print("${currentThread().name} 검색어를 입력하세요 (없으면 전체 출력, 종료는 q): ")
        return readln()
    }

    fun printIndexedPosts(posts: Posts) {
        posts.posts.forEachIndexed { index, post ->
            println("[${index + 1}] $post")
        }
        println("\n")
    }

    fun printNewPosts(posts: Posts) {
        println("\n")
        posts.posts.forEach { post ->
            println("[new] $post")
        }
        println("\n")
    }
}
