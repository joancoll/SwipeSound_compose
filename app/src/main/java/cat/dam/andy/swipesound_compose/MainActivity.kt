package cat.dam.andy.swipesound_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import kotlin.math.abs
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.Switch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.dam.andy.swipesound_compose.ui.theme.SwipeSound_composeTheme
import cat.dam.andy.swipesound_compose.viewmodel.MyViewModel
import cat.dam.andy.swipesound_compose.viewmodel.Sound

var detectGestures = true

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myViewModel: MyViewModel by viewModels()
        setContent {
            SwipeSound_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwipeDetector(myViewModel)
                }
            }
        }
    }
}

@Composable
fun SwipeDetector(myViewModel: MyViewModel = viewModel()) {
    val backgroundColor = myViewModel.backgroundColor.value
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Swipe to change color and play sound")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Background Music")
            Switch(checked = myViewModel.isMusicPlaying.value, onCheckedChange = { isChecked ->
                myViewModel.isMusicPlaying.value = isChecked
                myViewModel.toggleBackgroundMusic()
            })
            Button(content = { Text("Instructions") }, onClick = { myViewModel.playSound(Sound.INSTRUCTIONS) })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
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
                                        myViewModel.backgroundColor.value = Color.Green
                                        myViewModel.playSound(Sound.SWIPE_RIGHT)
                                    } else {
                                        // Swipe Left
                                        myViewModel.backgroundColor.value  = Color.Red
                                        myViewModel.playSound(Sound.SWIPE_LEFT)
                                    }
                                } else {
                                    // Vertical swipe
                                    if (directionY > 0) {
                                        // Swipe Down
                                        myViewModel.backgroundColor.value  = Color.Blue
                                        myViewModel.playSound(Sound.SWIPE_DOWN)
                                    } else {
                                        // Swipe Up
                                        myViewModel.backgroundColor.value  = Color.Yellow
                                        myViewModel.playSound(Sound.SWIPE_UP)
                                    }
                                }
                            }
                        }
                    }
                }
        )
    }
}
