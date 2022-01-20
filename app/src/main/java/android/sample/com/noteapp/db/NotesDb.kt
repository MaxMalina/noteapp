package android.sample.com.noteapp.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Database
import java.util.Date

import android.sample.com.noteapp.Constants.DATA_BASE_NAME
import android.sample.com.noteapp.Constants.DATE_FORMAT
import android.sample.com.noteapp.Constants.NOTES_DATA
import android.sample.com.noteapp.ioThread


@Database(entities = arrayOf(Note::class), version = 1)
abstract class NotesDb : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDb? = null

        fun getInstance(context: Context): NotesDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDb::class.java,
                    DATA_BASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            fillInDb(context.applicationContext)
                        }
                    })
                    .build()

                INSTANCE = instance
                return instance
            }
        }

        private fun fillInDb(context: Context) {
            ioThread {
                getInstance(context).noteDao().insertAllNotes(
                    NOTES_DATA.map {
                        Note(
                            id = 0,
                            text = it,
                            date = DATE_FORMAT.format(Date())
                        )
                    })
            }
        }
    }
}