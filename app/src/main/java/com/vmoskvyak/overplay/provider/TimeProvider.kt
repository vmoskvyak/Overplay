package com.vmoskvyak.overplay.provider

interface TimeProvider {
    fun currentTimeMillis(): Long
}
