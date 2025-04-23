import controller.Controller
import kotlinx.coroutines.Dispatchers
import repository.PostRepository
import repository.XmlParser
import view.View

fun main() {
    val view = View()
    val postRepository = PostRepository(XmlParser())
    val controller = Controller(view, postRepository, Dispatchers.IO)
    controller.run()
}
