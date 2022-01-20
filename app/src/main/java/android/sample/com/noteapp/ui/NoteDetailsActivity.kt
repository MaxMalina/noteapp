package android.sample.com.noteapp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity

import android.sample.com.noteapp.R
import android.sample.com.noteapp.databinding.ActivityNotesDetailBinding
import android.sample.com.noteapp.viewmodels.NoteViewModel
import android.sample.com.noteapp.Constants.KEY

class NoteDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesDetailBinding
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes_detail)
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        val extras = intent.extras
        if (extras != null) {
            binding.container.setText(extras.getString(KEY), TextView.BufferType.EDITABLE)
        }

        binding.save.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val newNote = binding.container.text.trim()
        if(newNote.isNotEmpty()){
            noteViewModel.saveNote(newNote)
        }
    }
}
