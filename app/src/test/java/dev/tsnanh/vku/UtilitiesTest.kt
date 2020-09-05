/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku

import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.utils.convertToDateString
import dev.tsnanh.vku.utils.hasValidAlarm
import dev.tsnanh.vku.utils.isValidWeek
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilitiesTest {
    @Test
    fun convertStringToDate_isCorrect() {
        assertThat((1599004800L * 1000).convertToDateString(), equalTo("2020/09/02 - 07:00:00"))
    }

    @Test
    fun checkSubjectHasValidAlarm_isCorrect() {
        val subject = Subject("", "", "", "", "", "", "Hai", "1->2", "B204")
        assertThat(subject.hasValidAlarm(), equalTo(true))
    }

    @Test
    fun checkSubjectHasValidAlarm_isNotCorrect() {
        val subject = Subject("", "", "", "", "", "", "_", "_", "_")
        assertThat(subject.hasValidAlarm(), equalTo(false))
    }

    @Test
    fun checkSubjectHasValidWeek_isCorrect() {
        val subject = Subject("", "", "", "", "", "5->20", "_", "_", "_")
        assertThat(subject.week.isValidWeek, equalTo(true))
    }

    @Test
    fun checkSubjectHasValidWeek_isNotCorrect() {
        val subject = Subject("", "", "", "", "", "_", "_", "_", "_")
        assertThat(subject.week.isValidWeek, equalTo(false))
    }
}
