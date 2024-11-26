package com.example.bookspdm.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bookspdm.R
import com.example.bookspdm.databinding.ActivityMainBinding
import com.example.bookspdm.model.Book
import com.example.bookspdm.model.Constant

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

    private lateinit var barl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        barl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode == RESULT_OK){
            val book = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                    result.data?.getParcelableExtra<Book>(Constant.BOOK)
                }
                else{
                    result.data?.getParcelableExtra(Constant.BOOK, Book::class.java)
                }
            book?.let{
                bookList.add(it) //adicionando um novo livro no final da lista que foi feito lá na onOptionsItemSelected
                bookAdapter.add(it.title)
                bookAdapter.notifyDataSetChanged()
            }
            }
        }


        amb.toolbarIn.toolbar.let{
            it.subtitle = "Book list"
            setSupportActionBar(it)
        }
        //fillBookList()
        amb.booksLv.adapter = bookAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // símbolo de igual = return, when = case
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.addBookMi ->{
            //Abrir tela para adicionar novo livro (clicando no +)
            barl.launch(Intent(this, BookActivity::class.java)) //vai dessa activity para a Book activity -> coloca os dados do livro novo e clica para adicionar ele
            true
        }
        else -> {
            false
        }
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