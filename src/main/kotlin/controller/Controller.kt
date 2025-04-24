package controller

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import repository.PostRepository
import view.DefaultPostView
import view.PostView
import kotlin.coroutines.cancellation.CancellationException

class Controller(
    private val view: PostView,
    private val repository: PostRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    fun run() =
        try {
            runBlocking {
                withContext(dispatcher) {
                    initialize()
                }
                launch(dispatcher) {
                    while (isActive) {
                        pollAndPrintNewPosts()
                    }
                }
                launch {
                    while (isActive) {
                        this@runBlocking.handleUserSearch()
                    }
                }
            }
        } catch (e: Exception) {
            when {
                e is CancellationException && e.message == DefaultPostView.QUIT_COMMAND -> view.printQuitProgramMessage()
                else -> view.printExceptionMessage()
            }
        }

    private suspend fun initialize() {
        view.printLoadingMessage()
        repository.initialize()
    }

    private suspend fun pollAndPrintNewPosts() {
        delay(10000L)
        repository.refresh()
        val newPosts = repository.getNewPosts()
        if (newPosts.isNotEmpty()) view.printNewPosts(newPosts)
    }

    private fun CoroutineScope.handleUserSearch() {
        val keyword = view.readKeyword()
        if (keyword == DefaultPostView.QUIT_COMMAND) {
            cancel(CancellationException(DefaultPostView.QUIT_COMMAND))
        } else {
            val searchResults = repository.searchPostsBy(keyword)
            view.printIndexedPosts(searchResults)
        }
    }
}
