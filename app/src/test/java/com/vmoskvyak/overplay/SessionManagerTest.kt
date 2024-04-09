package com.vmoskvyak.overplay

import com.vmoskvyak.overplay.manager.SessionManager
import com.vmoskvyak.overplay.provider.TestTimeProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var testTimeProvider: TestTimeProvider

    @Before
    fun setUp() {
        testTimeProvider = TestTimeProvider()
        sessionManager = SessionManager(testTimeProvider)
    }

    @Test
    fun `sessionCount starts from 1 on first app foreground`() {
        sessionManager.onAppForegrounded()

        assertEquals(1, sessionManager.sessionCount)
    }

    @Test
    fun `sessionCount does not increment if app is foregrounded within timeout interval`() {
        sessionManager.onAppForegrounded()

        testTimeProvider.addMillis(5 * 60 * 1000) // 5 minutes

        sessionManager.onAppBackgrounded()
        sessionManager.onAppForegrounded()

        assertEquals(1, sessionManager.sessionCount)
    }

    @Test
    fun `sessionCount increments by 1 if app is foregrounded after timeout interval`() {
        sessionManager.onAppForegrounded()
        sessionManager.onAppBackgrounded()

        testTimeProvider.addMillis(12 * 60 * 1000) // 12 minutes

        sessionManager.onAppForegrounded()

        assertEquals(2, sessionManager.sessionCount)
    }

    @Test
    fun `sessionCount accurately tracks multiple foreground events separated by more than timeout interval`() {
        sessionManager.onAppForegrounded()
        assertEquals(1, sessionManager.sessionCount)

        sessionManager.onAppBackgrounded()
        testTimeProvider.addMillis((SessionManager.SESSION_TIMEOUT_INTERVAL_MS + 1).toLong())
        sessionManager.onAppForegrounded()

        sessionManager.onAppBackgrounded()
        testTimeProvider.addMillis((SessionManager.SESSION_TIMEOUT_INTERVAL_MS + 1).toLong())
        sessionManager.onAppForegrounded()

        assertEquals(3, sessionManager.sessionCount)
    }
}
