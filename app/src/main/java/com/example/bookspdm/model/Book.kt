package com.example.bookspdm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//métodos get e set
//compare dele, compara os conteúdos do objeto, não o hash dele! p1 = p2
@Parcelize
data class Book (
    val title: String,//atributo, tudo que estiver entre () já será o título do livro
    val isbn: String,
    val firstAuthor: String,
    val publisher: String,
    val edition: Int,
    val pages: Int
    ): Parcelable