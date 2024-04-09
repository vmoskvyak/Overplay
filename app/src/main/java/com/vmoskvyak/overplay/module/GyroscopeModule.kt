package com.vmoskvyak.overplay.module

import android.content.Context
import android.hardware.SensorManager
import com.vmoskvyak.overplay.gyroscope.gyroscopeZRotationFlow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
object GyroscopeModule {

    @GyroscopeSensor
    @Provides
    fun provideGyroscopeFlow(@ApplicationContext context: Context): Flow<Float> {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return gyroscopeZRotationFlow(sensorManager)
    }
}

annotation class GyroscopeSensor
