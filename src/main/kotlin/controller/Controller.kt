package controller

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import repository.PostRepository
import view.View

class Controller(
    private val view: View,
    private val repository: PostRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    fun run() =
        runBlocking {
            withContext(dispatcher) {
                repository.initializeCache()
            }
            launch(dispatcher) {
                while (isActive) {
                    delay(10000L)
                    pollAndPrintNewPosts()
                }
            }
            launch {
                while (isActive) {
                    handleUserSearch()
                }
            }
        }

    private suspend fun pollAndPrintNewPosts() {
        repository.refreshCache()
        val newPosts = repository.getNewPosts()
        if (newPosts.isNotEmpty()) view.printNewPosts(newPosts)
    }

    private fun handleUserSearch() {
        val keyword = view.readKeyword()
        val searchResult = repository.searchPostsBy(keyword)
        view.printIndexedPosts(searchResult)
    }
}
