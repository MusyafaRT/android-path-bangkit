package com.dicoding.courseschedule.ui.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dicoding.courseschedule.R
import org.junit.Rule
import org.junit.Test


class HomeActivityTest{
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun testAddScheduleUI(){
        onView(withId(R.id.action_add)).check(matches(isDisplayed()))
        onView(withId(R.id.action_add)).perform(click())
        onView(withId(R.id.edCourseName)).check(matches(isDisplayed()))
        onView(withId(R.id.edLecturer)).check(matches(isDisplayed()))
        onView(withId(R.id.edNote)).check(matches(isDisplayed()))
    }
}