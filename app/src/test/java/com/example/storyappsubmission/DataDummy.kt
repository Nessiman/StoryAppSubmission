package com.example.storyappsubmission

import com.example.storyappsubmission.view.story.ListStoryItem

object DataDummy {
    fun generateDummyStory(): List<ListStoryItem>{
        val item = arrayListOf<ListStoryItem>()
        for (i in 1..10){
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1665555649719_1_Bqqmvm.jpg",
                "2022-10-12T06:20:49.720Z",
                "ess",
                "zz",
                106.64356,
                "story-cqEaMhCjjM5Ws20J",
                -6.1335033
            )
            item.add(story)
        }
        return item
    }
}