import controller.Controller
import kotlinx.coroutines.Dispatchers
import repository.DefaultPostRepository
import repository.local.PostsCache
import repository.remote.XmlParser
import view.DefaultPostView

fun main() {
    val view = DefaultPostView
    val repository = DefaultPostRepository(XmlParser(), PostsCache())
    val controller = Controller(view, repository, Dispatchers.IO)
    controller.run()
}
