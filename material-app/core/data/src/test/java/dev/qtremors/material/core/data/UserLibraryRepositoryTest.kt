package dev.qtremors.material.core.data

import org.junit.Assert.assertEquals
import org.junit.Test

class UserLibraryRepositoryTest {
    @Test
    fun recentEntriesAreNewestFirstUniqueAndCappedAtTwenty() {
        val fullHistory = (1..25).map { "component-$it" }

        assertEquals(
            listOf("component-21") + (1..19).map { "component-$it" },
            updatedRecent(fullHistory, "component-21"),
        )
        assertEquals(20, updatedRecent(fullHistory, "new-component").size)
        assertEquals("new-component", updatedRecent(fullHistory, "new-component").first())
    }

    @Test
    fun addingToEmptyHistoryCreatesOneRecentEntry() {
        assertEquals(listOf("buttons"), updatedRecent(emptyList(), "buttons"))
    }
}
