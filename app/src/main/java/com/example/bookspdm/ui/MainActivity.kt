package com.example.bookspdm.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.bookspdm.databinding.ActivityMainBinding
import com.example.bookspdm.model.Book

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source (list)
    private val bookList: MutableList<Book> = mutableListOf()

    //Adapter = mutable list não aceita tipo Book.
    private val bookAdapter: ArrayAdapter<String> by lazy {
        val bookTitleList: MutableList<String> = mutableListOf()
        bookList.forEach { book -> bookTitleList.add(book.title) } //se book.ToString() mostraria todas as infos do books
        ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            bookTitleList
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        fillBookList() //lista preenchida com books genéricos

        amb.booksLv.adapter = bookAdapter //associa bookAdapter com a ListView

    }

    private fun fillBookList(){
        for(index in 1..50){
            bookList.add(
                Book(
                    "Title $index",
                    "ISBN $index",
                    "Author $index",
                    "Publisher $index",
                    index,
                    index
                )
            )
        }
    }

}