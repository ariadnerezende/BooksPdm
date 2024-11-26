package com.example.bookspdm.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookspdm.R
import com.example.bookspdm.databinding.ActivityBookBinding
import com.example.bookspdm.model.Book
import com.example.bookspdm.model.Constant
import com.example.bookspdm.model.Constant.BOOK

class BookActivity : AppCompatActivity() {
    private val abb: ActivityBookBinding by lazy {
        ActivityBookBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(abb.root)

        val receiveBook = intent.getParcelableExtra<Book>(BOOK)
        receiveBook?.let {
            book -> with(abb){
                titleEt.setText(book.title)
                isbnEt.setText(book.isbn)
                isbnEt.isEnabled = false; //isbn não pode ser editável.
                firstAuthorEt.setText((book.firstAuthor))
                publisherEt.setText(book.publisher)
                editionEt.setText(book.edition.toString())
                pagesEt.setText(book.pages.toString())
            }
        }

        abb.toolBarIn.toolbar.let {
            it.subtitle = if(receiveBook == null) "New Book" else "Edit Book"
            setSupportActionBar(it)
        }

        //Salvando um livro novo
        abb.run {
            saveBt.setOnClickListener {
                val newBook = Book(
                    titleEt.text.toString(),
                    isbnEt.text.toString(),
                    firstAuthorEt.text.toString(),
                    publisherEt.text.toString(),
                    editionEt.text.toString().toInt(),
                    pagesEt.text.toString().toInt()
                )
                Intent().apply {
                    putExtra("BOOK", newBook)
                    setResult(RESULT_OK, this)
                    finish()
                }
            }
        }
    }
}