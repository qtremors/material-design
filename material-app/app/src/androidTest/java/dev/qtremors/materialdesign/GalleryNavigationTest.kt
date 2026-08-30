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

    @Test
    fun guidanceTextFindsAFoundationAndExplainsItsBehavior() {
        composeRule.onNodeWithText("Search Material Design").performTextInput("spring physics")
        composeRule.onNodeWithText("Motion").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Guidance").performClick()
        composeRule.onNodeWithText("Behavior and feeling").assertIsDisplayed()
        composeRule.onNodeWithText("Motion is user-triggered, interruptible, and safe under rapid repeated input.")
            .assertIsDisplayed()
    }

    @Test
    fun foundationInspectorExposesLargeTextAndTouchTargetReferences() {
        composeRule.onNodeWithText("Search Material Design").performTextInput("talkback")
        composeRule.onNodeWithText("Accessibility").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Large text and reflow").assertIsDisplayed()
        composeRule.onNodeWithText("Touch target versus visible icon").assertIsDisplayed()
    }

    @Test
    fun textFieldAliasOpensInteractiveValidationReference() {
        composeRule.onNodeWithText("Search Material Design").performTextInput("form field")
        composeRule.onNodeWithText("Text fields").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Validation").assertIsDisplayed()
        composeRule.onNodeWithText("Review email").assertIsDisplayed()
    }

    @Test
    fun dialogGuidanceExplainsFocusContainment() {
        composeRule.onNodeWithText("Search Material Design").performTextInput("destructive confirmation")
        composeRule.onNodeWithText("Dialogs").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Guidance").performClick()
        composeRule.onNodeWithText("Focus remains within the dialog while open and reaches a safe initial control.")
            .assertIsDisplayed()
    }
}
