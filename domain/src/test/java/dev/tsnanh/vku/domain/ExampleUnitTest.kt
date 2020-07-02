package dev.tsnanh.vku.domain

import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `is forum name equal "Cat"`() {
        GlobalScope.launch {
            val firstForum = VKUServiceApi.network.getForums()[0]
            assertEquals("Cat", firstForum.title)
        }
    }
}