package android.sample.com.noteapp.ui

import android.sample.com.noteapp.db.Note

interface OnEditClickListener {
    fun onEditClicked(note: Note)
}

interface OnShareClickListener {
    fun onShareClicked(note: Note)
}

interface OnDeleteClickListener {
    fun onDeleteClicked(note: Note)
}