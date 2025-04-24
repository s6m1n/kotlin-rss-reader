package model

import java.time.LocalDate

data class Post(
    val title: String,
    val link: String,
    val date: LocalDate,
)
