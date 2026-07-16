package dev.qtremors.materialdesign

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.accessibility.enableAccessibilityChecks
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.semantics.SemanticsActions
import org.junit.Rule
import org.junit.Before
import org.junit.Test

class GalleryNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun enableAccessibilityValidation() {
        composeRule.enableAccessibilityChecks()
    }

    @Test
    fun searchFindsCanonicalFabEntryFromAlias() {
        composeRule.onNodeWithText("Search Material Design").performTextInput("fab")
        composeRule.onNodeWithText("Floating action buttons").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Interactive reference").assertIsDisplayed()
    }

    @Test
    fun compactFloatingNavigationChangesRootDestination() {
        composeRule.onNodeWithContentDescription("Catalog").performClick()
        composeRule.onNodeWithText("All").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Foundations").performClick()
        composeRule.onNodeWithText("Inspectable foundations appear here only when a working reference exists.").assertIsDisplayed()
    }

    @Test
    fun settingsIsAnAppSurfaceNotAnAccount() {
        composeRule.onNodeWithContentDescription("App settings").performClick()
        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Dynamic color").assertIsDisplayed()
        composeRule.onNodeWithText("Material Design").assertIsDisplayed()
    }

    @Test
    fun holdConfirmationHasAnAccessibleNonGestureAction() {
        composeRule.onNodeWithText("Search Material Design").performTextInput("expressive buttons")
        composeRule.onNodeWithText("Buttons").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Hold to confirm")
            .performSemanticsAction(SemanticsActions.OnLongClick)
        composeRule.onNodeWithText("Confirmed").assertIsDisplayed()
    }
}
