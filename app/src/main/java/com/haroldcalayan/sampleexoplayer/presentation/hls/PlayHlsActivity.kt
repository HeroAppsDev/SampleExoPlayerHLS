package com.haroldcalayan.sampleexoplayer.presentation.hls

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.haroldcalayan.sampleexoplayer.ui.theme.SampleExoplayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayHlsActivity : ComponentActivity() {
    private val playerViewModel: PlayerViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @kotlin.OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleExoplayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                title = {
                                    Text(
                                        text = "HLS Player Sample",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            )
                        }
                    ) {
                        PlayerScreen(playerViewModel)
                    }
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    @Composable
    fun PlayerScreen(viewModel: PlayerViewModel = hiltViewModel()) {
        var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event -> lifecycle = event }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                viewModel.exoPlayer.release()
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Spacer(modifier = Modifier.height(80.dp))
            Player(viewModel = viewModel)
        }
    }

    @Composable
    fun Player(viewModel: PlayerViewModel) {
        var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
        val exoPlayer = viewModel.exoPlayer
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event -> lifecycle = event }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                exoPlayer.release()
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            factory = { PlayerView(context).apply { player = exoPlayer } },
            update = { playerView ->
                when (lifecycle) {
                    Lifecycle.Event.ON_RESUME -> {
                        playerView.onResume()
                        exoPlayer.play()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        playerView.onPause()
                        exoPlayer.pause()
                    }
                    else -> Unit
                }
            }
        )
    }
}
