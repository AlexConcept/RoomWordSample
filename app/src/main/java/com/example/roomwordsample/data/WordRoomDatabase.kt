package com.example.roomwordsample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class WordRoomDatabase {
    @Database(entities = [Word::class], version = 1, exportSchema = false)
    abstract class WordRoomDatabase : RoomDatabase() {
        abstract fun wordDao(): WordDao
    }

    private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            wordDao.deleteAll()
            var word = Word(1, "Hello")
            wordDao.insert(word)
            word = Word(2, "Word")
            wordDao.insert(word)
            word = Word(3, "TODO!")
            wordDao.insert(word)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WordRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                instance

            }
        }
    }
}
