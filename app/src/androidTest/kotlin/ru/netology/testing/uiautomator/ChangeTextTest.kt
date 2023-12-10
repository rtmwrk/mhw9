package ru.netology.testing.uiautomator

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random


const val SETTINGS_PACKAGE = "com.android.settings"
const val MODEL_PACKAGE = "ru.netology.testing.uiautomator"

const val TIMEOUT = 25000L

@RunWith(AndroidJUnit4::class)
class ChangeTextTest {

    private lateinit var device: UiDevice
    private val textToSet = "Netology"

    private var emptyString = ""

    private fun waitForPackage(packageName: String) {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }

    @Before
    fun beforeEachTest() {
        // Press home
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        // Wait for launcher
        val launcherPackage = device.launcherPackageName
        device.wait(Until.hasObject(By.pkg(launcherPackage)), TIMEOUT)
    }

    @Test
    fun testInternetSettings() {
        waitForPackage(SETTINGS_PACKAGE)

        device.findObject(
            UiSelector().resourceId("android:id/title").instance(0)
        ).click()
    }

    @Test
    fun testChangeText() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        device.findObject(By.res(packageName, "userInput")).text = textToSet
        device.findObject(By.res(packageName, "buttonChange")).click()

        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        assertEquals(result, textToSet)
    }

    // --- Exercise 1. task 1. -----------------------------------------------------
    // It's block of tests on empty string
    @Test
    // --- Input empty string
    fun testNotChangeWithEmptyString() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        // Read old header
        val oldHeader = device.findObject(By.res(packageName, "textToBeChanged")).text
        // Input empty string
        device.findObject(By.res(packageName, "userInput")).text = emptyString
        // Click on "Change" button
        device.findObject(By.res(packageName, "buttonChange")).click()
        // Read new header
        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        // New header have equal old header
        assertEquals(result, oldHeader)
    }

    // --- Input string with 1-e space
    @Test
    fun testNotChangeWithOneSpaceString() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        // Read old header
        val oldHeader = device.findObject(By.res(packageName, "textToBeChanged")).text
        // Input string with 1-e space
        device.findObject(By.res(packageName, "userInput")).text = emptyString + " "
        // Click on "Change" button
        device.findObject(By.res(packageName, "buttonChange")).click()
        // Read new header
        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        // New header have equal old header
        assertEquals(result, oldHeader)
    }

    // --- Input string with arbitrary quantity space
    @Test
    fun testNotChangeWithArbitrarySpaceString() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        // Read old header
        val oldHeader = device.findObject(By.res(packageName, "textToBeChanged")).text
        // Input string with n-space
        val lengthString = Random.nextInt(1, 25)
        for(i in 0..lengthString - 1) {
            emptyString += " "
        }
        device.findObject(By.res(packageName, "userInput")).text = emptyString
        // Click on "Change" button
        device.findObject(By.res(packageName, "buttonChange")).click()
        // Read new header
        val result = device.findObject(By.res(packageName, "textToBeChanged")).text
        // New header have equal old header
        assertEquals(result, oldHeader)
    }

    // --- Exercise 1. task 2. -----------------------------------------------------
    // It's block of tests on "Another activity button"
    @Test
    fun testAnotherActivity() {
        val packageName = MODEL_PACKAGE
        waitForPackage(packageName)

        // Input test in test field of form
        device.findObject(By.res(packageName, "userInput")).text = textToSet
        // Click on activity button
        device.findObject(By.res(packageName, "buttonActivity")).click()
        // Wait activity (wait resource-id = "text"
        device.wait(Until.hasObject(By.res(packageName, "text")), TIMEOUT)
        // Read test in activity
        val result = device.findObject(By.res(packageName, "text")).text
        // Text in activity have equal input string
        assertEquals(result, textToSet)
    }
}



