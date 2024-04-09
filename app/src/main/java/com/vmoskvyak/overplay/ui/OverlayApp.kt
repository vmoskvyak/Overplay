package com.vmoskvyak.overplay.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.vmoskvyak.overplay.R
import com.vmoskvyak.overplay.ui.viewmodel.RotatePosition
import com.vmoskvyak.overplay.ui.viewmodel.SessionUIState
import com.vmoskvyak.overplay.ui.viewmodel.SessionViewModel

@Composable
fun OverlayApp(viewModel: SessionViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val sessionUIState by viewModel.sessionUiState.collectAsState()

    LifecycleAwareSessionUpdater(lifecycleOwner, viewModel)
    SessionCounter(sessionUIState)
}

@Composable
private fun LifecycleAwareSessionUpdater(
    lifecycleOwner: LifecycleOwner,
    viewModel: SessionViewModel
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.onAppForegrounded()
                Lifecycle.Event.ON_PAUSE -> viewModel.onAppBackgrounded()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun SessionCounter(sessionUIState: SessionUIState) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val targetTextSize = when (sessionUIState.rotatePosition) {
            RotatePosition.LEFT -> 12.sp
            RotatePosition.RIGHT -> 20.sp
            else -> 16.sp
        }

        val animatedTextSize by animateFloatAsState(
            targetValue = targetTextSize.value,
            animationSpec = tween(durationMillis = 300),
            label = ""
        )

        Text(
            text = stringResource(R.string.session_count, sessionUIState.sessionCount),
            style = TextStyle(fontSize = animatedTextSize.sp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OverlayAppPreview() {
    SessionCounter(SessionUIState(5, RotatePosition.DEFAULT))
}
