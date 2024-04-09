package com.vmoskvyak.overplay.manager

import com.vmoskvyak.overplay.provider.TimeProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val timeProvider: TimeProvider
) {
    internal var sessionCount = 1
    private var lastSessionTimestamp: Long = timeProvider.currentTimeMillis()

    fun onAppForegrounded() {
        if (timeProvider.currentTimeMillis() - lastSessionTimestamp > SESSION_TIMEOUT_INTERVAL_MS) {
            sessionCount++
        }
    }

    fun onAppBackgrounded() {
        lastSessionTimestamp = timeProvider.currentTimeMillis()
    }

    companion object {
        const val SESSION_TIMEOUT_INTERVAL_MS = 10 * 60 * 1000 // 10 minutes in milliseconds
    }
}
