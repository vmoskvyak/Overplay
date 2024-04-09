package com.vmoskvyak.overplay.provider

import com.vmoskvyak.overplay.provider.TimeProvider

class TestTimeProvider : TimeProvider {
    private var time = 0L

    override fun currentTimeMillis() = time

    fun addMillis(millisToAdd: Long) {
        time += millisToAdd
    }
}