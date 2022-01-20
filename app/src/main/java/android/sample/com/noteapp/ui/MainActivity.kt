package android.sample.com.noteapp.ui

import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView

import android.sample.com.noteapp.Constants.KEY
import android.sample.com.noteapp.Constants.SHARING_TEXT
import android.sample.com.noteapp.Constants.SHARING_TYPE
import android.sample.com.noteapp.R
import android.sample.com.noteapp.viewmodels.NoteViewModel
import android.sample.com.noteapp.databinding.ActivityMainBinding
import android.sample.com.noteapp.db.Note


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    OnEditClickListener,
    OnDeleteClickListener,
    OnShareClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        val adapter = NotesAdapter()
        noteViewModel.allNotes().observe(this, Observer {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        binding.add.setOnClickListener {
            val intent = Intent(this, NoteDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = binding.recycler.adapter as NotesAdapter
        when (item.itemId) {
            R.id.action_asc -> {
                adapter.sort()
                adapter.notifyDataSetChanged()
                return true
            }
            R.id.action_dsc -> {
                adapter.reverse()
                adapter.notifyDataSetChanged()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        (binding.recycler.adapter as NotesAdapter).filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        (binding.recycler.adapter as NotesAdapter).filter.filter(newText)
        return true
    }

    override fun onEditClicked(note: Note) {
        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra(KEY, note.text)
        noteViewModel.deleteNote(note)
        startActivity(intent)
    }

    override fun onShareClicked(note: Note) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = SHARING_TYPE
        sharingIntent.putExtra(Intent.EXTRA_TEXT, note.text)
        startActivity(Intent.createChooser(sharingIntent, SHARING_TEXT))
    }

    override fun onDeleteClicked(note: Note) {
        noteViewModel.deleteNote(note)
    }
}
