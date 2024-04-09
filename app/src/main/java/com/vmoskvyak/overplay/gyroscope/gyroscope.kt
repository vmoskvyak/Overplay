package com.vmoskvyak.overplay.gyroscope

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun gyroscopeZRotationFlow(sensorManager: SensorManager): Flow<Float> = callbackFlow {
    val gyroScopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    var lastTimestamp = SystemClock.elapsedRealtimeNanos()
    var currentRotationZ = 0f

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                val now = SystemClock.elapsedRealtimeNanos()
                // multiplied by to convert the elapsed time from nanoseconds to seconds
                val elapsedTimeInSeconds = (now - lastTimestamp) * 1e-9
                lastTimestamp = now

                // event.values[2] is the rotation rate around the Z-axis in radians per second.
                // Convert this rate to degrees and integrate over time to estimate the angle.
                currentRotationZ += Math.toDegrees(event.values[2].toDouble() * elapsedTimeInSeconds)
                    .toFloat()

                trySend(currentRotationZ)
            }
        }
    }

    sensorManager.registerListener(
        sensorEventListener,
        gyroScopeSensor,
        SensorManager.SENSOR_DELAY_UI
    )

    awaitClose {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
