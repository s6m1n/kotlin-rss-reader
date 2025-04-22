
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.parseRssPubDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val parsedDate = ZonedDateTime.parse(this, inputFormatter)
    return outputFormatter.format(parsedDate)
}
