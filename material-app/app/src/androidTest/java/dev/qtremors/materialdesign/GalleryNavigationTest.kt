package dev.qtremors.materialdesign

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.junit4.accessibility.enableAccessibilityChecks
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.semantics.SemanticsActions
import org.junit.Rule
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class GalleryNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun enableAccessibilityValidation() {
        composeRule.enableAccessibilityChecks()
    }

    /** Opens the top-bar search and types into its editable field. */
    private fun search(query: String) {
        val searchAction = hasContentDescription("Search components")
        // The top bar appears once the bundled catalog has loaded.
        composeRule.waitUntilAtLeastOneExists(searchAction, timeoutMillis = 10_000)
        composeRule.onNode(searchAction).performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput(query)
    }

    /** Waits for the debounced results, then opens the entry through its click action. */
    private fun openResult(officialName: String) {
        val result = hasText(officialName) and hasClickAction()
        composeRule.waitUntilAtLeastOneExists(result, timeoutMillis = 5_000)
        composeRule.onAllNodes(result).onFirst().performSemanticsAction(SemanticsActions.OnClick)
        composeRule.waitForIdle()
    }

    @Test
    fun searchFindsCanonicalFabEntryFromAlias() {
        search("fab")
        openResult("Floating action button")
        composeRule.onNodeWithText("Preview").assertIsDisplayed()
        composeRule.onNodeWithText("Every state below is live", substring = true).assertIsDisplayed()
    }

    @Test
    fun compactFloatingNavigationChangesRootDestination() {
        composeRule.waitUntilAtLeastOneExists(hasContentDescription("Catalog"), timeoutMillis = 10_000)
        composeRule.onNodeWithContentDescription("Catalog").performClick()
        composeRule.onNodeWithText("All").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Foundations").performClick()
        composeRule.onNodeWithText("Inspectable foundations appear here only when a working reference exists.").assertIsDisplayed()
    }

    @Test
    fun settingsIsAnAppSurfaceNotAnAccount() {
        composeRule.waitUntilAtLeastOneExists(hasContentDescription("Open settings"), timeoutMillis = 10_000)
        composeRule.onNodeWithContentDescription("Open settings").performClick()
        // The page title and the floating Back pill both name the page.
        composeRule.onAllNodes(hasText("Settings")).onFirst().assertIsDisplayed()
        composeRule.onNodeWithText("Appearance").assertIsDisplayed()
        composeRule.onAllNodes(hasText("Sign in", substring = true)).assertCountEquals(0)
    }

    @Test
    fun holdConfirmationHasAnAccessibleNonGestureAction() {
        search("expressive buttons")
        openResult("Button")
        composeRule.onNodeWithText("Hold to confirm")
            .performSemanticsAction(SemanticsActions.OnLongClick)
        composeRule.onNodeWithText("Confirmed").assertIsDisplayed()
    }

    @Test
    fun guidanceTextFindsAFoundationAndExplainsItsBehavior() {
        search("spring physics")
        openResult("Motion")
        composeRule.onNodeWithText("Guidance").performClick()
        composeRule.onNodeWithText("Behavior and feel").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Motion is user-triggered, interruptible, and safe under rapid repeated input.")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun foundationInspectorExposesLargeTextAndTouchTargetReferences() {
        search("talkback")
        openResult("Accessibility")
        composeRule.onNodeWithText("Large text and reflow").assertIsDisplayed()
        composeRule.onNodeWithText("Touch target versus visible icon").assertIsDisplayed()
    }

    @Test
    fun textFieldAliasOpensInteractiveValidationReference() {
        search("form field")
        openResult("Text field")
        composeRule.onNodeWithText("Validation").assertIsDisplayed()
        composeRule.onNodeWithText("Review email").assertIsDisplayed()
    }

    @Test
    fun dialogGuidanceExplainsFocusContainment() {
        search("destructive confirmation")
        openResult("Dialog")
        composeRule.onNodeWithText("Guidance").performClick()
        composeRule.onNodeWithText("Focus remains within the dialog while open and reaches a safe initial control.")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
