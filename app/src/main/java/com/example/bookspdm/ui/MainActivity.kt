package com.example.bookspdm.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bookspdm.R
import com.example.bookspdm.databinding.ActivityMainBinding
import com.example.bookspdm.model.Book
import com.example.bookspdm.model.Constant
import com.example.bookspdm.model.Constant.BOOK

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source (list)
    private val bookList: MutableList<Book> = mutableListOf()

    //Adapter = mutable list não aceita tipo Book.
    private val bookAdapter: BookAdapter by lazy {
        BookAdapter(this, bookList)
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
            //verificando se o livro existe
            book?.let{ receiveBook ->
                val position = bookList.indexOfFirst {  it.isbn == receiveBook.isbn}
                if(position == -1){
                    //adicionando
                    bookList.add(receiveBook)
                }
                else{
                    //substituindo
                    bookList[position] = receiveBook
                }
                //notifico o adapter sobre o que fiz
                bookAdapter.notifyDataSetChanged()
            }
            }
        }


        amb.toolbarIn.toolbar.let{
            it.subtitle = "Book list"
            setSupportActionBar(it)
        }
        fillBookList()
        amb.booksLv.adapter = bookAdapter

        //vai utilizar menu de contexto - edit e remove
        registerForContextMenu(amb.booksLv)
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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) = menuInflater.inflate(R.menu.context_menu_main, menu)


    //equivalente a onOptionsItemSelected
    override fun onContextItemSelected(item: MenuItem): Boolean {

        //Pegando a posição do livro que quero editar/remover
        val position = (item.menuInfo as AdapterContextMenuInfo).position

        return when(item.itemId){
            R.id.editBookMi -> {
                //Chamar tela de edição de livro
                //Mandando o livro junto com a Intent
                Intent(this, BookActivity::class.java).apply {
                    putExtra(BOOK, bookList[position])
                    barl.launch(this)
                }
                true
            }
            R.id.removeBookMi ->{
                //Remover livro da lista
                bookList.removeAt(position)
                bookAdapter.notifyDataSetChanged()
                true
            }
            else ->{
                false
            }
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