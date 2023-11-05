package com.example.storyappsubmission.view.main

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyappsubmission.data.network.ApiService
import com.example.storyappsubmission.data.pref.UserPreference
import com.example.storyappsubmission.view.story.ListStoryItem

class StoryPagingSource(private val apiService: ApiService, private val context: Context): PagingSource<Int, ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        val anchorPage = state.anchorPosition?.let { state.closestPageToPosition(it) }
        return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val userPreference = UserPreference(context)
        val userToken = userPreference.getUser()

        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer $userToken"
            val responseData = apiService.getStories(token, position, params.loadSize).listStory

            val filteredData = responseData?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = filteredData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (filteredData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}