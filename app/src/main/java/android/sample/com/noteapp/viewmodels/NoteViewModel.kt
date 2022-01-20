package android.sample.com.noteapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.AndroidViewModel
import java.util.*

import android.sample.com.noteapp.Constants
import android.sample.com.noteapp.db.Note
import android.sample.com.noteapp.db.NotesDb
import android.sample.com.noteapp.ioThread


class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NotesDb.getInstance(application).noteDao()

    fun allNotes(): LiveData<List<Note>> {
        return dao.loadAllNotes()
    }

    fun saveNote(text: CharSequence) = ioThread {
        dao.insertNote(Note(id = 0, text = text.toString(), date = Constants.DATE_FORMAT.format(Date())))
    }

    fun deleteNote(note: Note) = ioThread {
        dao.delete(note)
    }
}
