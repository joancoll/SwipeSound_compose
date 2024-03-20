package cat.dam.andy.swipesound_compose

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import cat.dam.andy.swipesound_compose.ui.theme.SwipeSound_composeTheme
import java.lang.Math.abs

var detectGestures = true

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwipeSound_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwipeDetector()
                }
            }
        }
    }
}

@Composable
fun SwipeDetector() {
    val context = LocalContext.current
    val backgroundColor = remember { mutableStateOf(Color.White) }
    val swipeLeftSound = MediaPlayer.create(context, R.raw.left)
    val swipeRightSound = MediaPlayer.create(context, R.raw.right)
    val swipeUpSound = MediaPlayer.create(context, R.raw.up)
    val swipeDownSound = MediaPlayer.create(context, R.raw.down)
    val instructionsSound = MediaPlayer.create(context, R.raw.instructions)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Swipe to change color and play sound")
        Button(content = { Text("Instructions") }, onClick = { playSound(instructionsSound) })
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    if (detectGestures) {
                        detectDragGestures { change, dragAmount ->
                            val directionX = change.position.x - change.previousPosition.x
                            val directionY = change.position.y - change.previousPosition.y

                            if (abs(directionX - directionY) > 0) {
                                // Horizontal or vertical swipe
                                if (abs(directionX) > abs(directionY)) {
                                    // Horizontal swipe
                                    if (directionX > 0) {
                                        // Swipe Right
                                        backgroundColor.value = Color.Green
                                        playSound(swipeRightSound)
                                    } else {
                                        // Swipe Left
                                        backgroundColor.value = Color.Red
                                        playSound(swipeLeftSound)
                                    }
                                } else {
                                    // Vertical swipe
                                    if (directionY > 0) {
                                        // Swipe Down
                                        backgroundColor.value = Color.Blue
                                        playSound(swipeDownSound)
                                    } else {
                                        // Swipe Up
                                        backgroundColor.value = Color.Yellow
                                        playSound(swipeUpSound)
                                    }
                                }
                            }
                        }
                    }
                }
                .background(backgroundColor.value)
        )
    }
}

fun playSound(sound: MediaPlayer) {
    sound.start()
}