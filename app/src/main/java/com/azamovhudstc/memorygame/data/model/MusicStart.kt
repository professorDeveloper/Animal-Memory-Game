package com.azamovhudstc.memorygame.data.model

import android.content.Context
import android.media.MediaPlayer
import com.azamovhudstc.memorygame.R

object BackMusic {

    private var mediaPlayerWrong = MediaPlayer()
    private var mediaPlayerWin = MediaPlayer()

    fun create(context: Context) {
        mediaPlayerWrong=MediaPlayer.create(context,R.raw.wrong_answer)
    }
    fun wrongMusicStart(){
        mediaPlayerWrong.start()
    }
    fun createWinMusic(context: Context){
        mediaPlayerWin=MediaPlayer.create(context,R.raw.right_answer)

    }
    fun startWinMusic(){
        mediaPlayerWin.start()
    }
}