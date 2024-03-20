package cat.dam.andy.swipesound_compose.viewmodel

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import cat.dam.andy.swipesound_compose.R

enum class Sound {
    SWIPE_LEFT,
    SWIPE_RIGHT,
    SWIPE_UP,
    SWIPE_DOWN,
    INSTRUCTIONS,
    MUSIC_BACKGROUND
}

class MyViewModel(application: Application) : AndroidViewModel(application) {

    val isMusicPlaying = mutableStateOf(true)
    var backgroundColor = mutableStateOf(Color.Cyan)
    var swipeDirection = mutableStateOf("")

    private val sounds = mapOf(
        Sound.SWIPE_LEFT to MediaPlayer.create(application, R.raw.left),
        Sound.SWIPE_RIGHT to MediaPlayer.create(application, R.raw.right),
        Sound.SWIPE_UP to MediaPlayer.create(application, R.raw.up),
        Sound.SWIPE_DOWN to MediaPlayer.create(application, R.raw.down),
        Sound.INSTRUCTIONS to MediaPlayer.create(application, R.raw.instructions),
        Sound.MUSIC_BACKGROUND to MediaPlayer.create(application, R.raw.music_separation_by_williamking).apply {
            isLooping = true
            setVolume(0.2f, 0.2f)
            if (isMusicPlaying.value) start()
        }
    )

    fun playSound(sound: Sound) {
        sounds[sound]?.let {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun toggleBackgroundMusic() {
        viewModelScope.launch {
            if (isMusicPlaying.value) {
                sounds[Sound.MUSIC_BACKGROUND]?.start()
            } else {
                sounds[Sound.MUSIC_BACKGROUND]?.pause()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Allibera els recursos dels MediaPlayer quan el ViewModel es destrueix
        sounds.values.forEach { it.release() }
    }
}