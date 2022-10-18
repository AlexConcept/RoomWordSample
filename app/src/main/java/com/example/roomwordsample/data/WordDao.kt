package com.example.roomwordsample.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy as OnConflictStrategy

class WordDao {
    @Dao
    interface WordDao {
        @Query("SELECT * FROM word_table ORDER BY word ASC")
        fun getAlphabetizedWords(): List<Word>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(word: Word)

        @Query("DELETE FROM word_table")
        suspend fun deleteAll()
    }
}