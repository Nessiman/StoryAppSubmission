package com.example.storyappsubmission.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyappsubmission.view.story.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM story")
    fun getALlStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}