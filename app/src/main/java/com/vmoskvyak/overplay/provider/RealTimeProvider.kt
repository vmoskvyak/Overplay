package com.vmoskvyak.overplay.provider

class RealTimeProvider : TimeProvider {
    override fun currentTimeMillis() = System.currentTimeMillis()
}
