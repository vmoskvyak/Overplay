package com.vmoskvyak.overplay.module

import com.vmoskvyak.overplay.provider.RealTimeProvider
import com.vmoskvyak.overplay.provider.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider = RealTimeProvider()
}
