package view

import model.Posts

interface PostView {
    fun printLoadingMessage()

    fun printQuitProgramMessage()

    fun printExceptionMessage()

    fun readKeyword(): String

    fun printIndexedPosts(posts: Posts)

    fun printNewPosts(posts: Posts)
}
