package com.example.alarmmanager
import android.content.Context
import android.media.MediaPlayer

class MusicControl(private var mediaPlayer: MediaPlayer?) {

    companion object {
        @Volatile
        private var INSTANCE: MusicControl? = null

        fun getInstance(context: Context): MusicControl {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = MusicControl(MediaPlayer.create(context,R.raw.music1))
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun playMusic(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.music1)
        mediaPlayer?.start()
    }

    fun stopMusic() {
        INSTANCE?.mediaPlayer?.stop()
        INSTANCE?.mediaPlayer?.seekTo(0)
    }
}


