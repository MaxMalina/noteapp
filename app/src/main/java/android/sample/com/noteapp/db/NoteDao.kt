package android.sample.com.noteapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update


@Dao
interface NoteDao {
    @Query("SELECT * from notes")
    fun loadAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllNotes(posts: List<Note>)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Delete
    fun deleteAllNotes(notes: List<Note>)
}