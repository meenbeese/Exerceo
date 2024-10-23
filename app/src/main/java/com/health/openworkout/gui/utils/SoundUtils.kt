package com.health.openworkout.gui.utils

import android.content.Context
import android.content.res.AssetManager
import android.media.SoundPool
import android.speech.tts.TextToSpeech
import android.util.Log

import java.io.IOException
import java.util.Locale

class SoundUtils(private val context: Context) {

    enum class SOUND {
        WORKOUT_COUNT_BEFORE_START,
        WORKOUT_START,
        WORKOUT_STOP,
        SESSION_COMPLETED
    }

    private val soundPool: SoundPool = SoundPool.Builder().setMaxStreams(NUMBER_OF_SIMULTANEOUS_SOUNDS).build()
    private val assetManager: AssetManager = context.assets
    private var tts: TextToSpeech? = null
    private var ttsInit = false

    private val soundIds = mutableMapOf<SOUND, Int>()

    private val TAG = SoundUtils::class.java.simpleName

    init {
        initializeTTS()
        loadSounds()
    }

    private fun initializeTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
                ttsInit = true
            } else {
                Log.e(TAG, "Text-to-Speech initialization failed")
            }
        }
    }

    private fun loadSounds() {
        SOUND.values().forEach { sound ->
            val fileName = when (sound) {
                SOUND.WORKOUT_COUNT_BEFORE_START -> "sound/workout.mp3"
                SOUND.WORKOUT_START -> "sound/workout_start.mp3"
                SOUND.WORKOUT_STOP -> "sound/workout_stop.mp3"
                SOUND.SESSION_COMPLETED -> "sound/session_completed.mp3"
            }
            loadSound(fileName)?.let { soundId ->
                soundIds[sound] = soundId
            }
        }
    }

    private fun loadSound(fileName: String): Int? {
        return try {
            assetManager.openFd(fileName).use { afd ->
                soundPool.load(afd.fileDescriptor, afd.startOffset, afd.length, 1)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to load sound: $fileName", e)
            null
        }
    }

    fun playSound(sound: SOUND) {
        soundIds[sound]?.let { soundId ->
            soundPool.play(soundId, LEFT_VOLUME_VALUE, RIGHT_VOLUME_VALUE, SOUND_PLAY_PRIORITY, MUSIC_LOOP, PLAY_RATE)
        } ?: Log.e(TAG, "Sound ID not found for $sound")
    }

    fun textToSpeech(speech: String?) {
        if (ttsInit && !speech.isNullOrEmpty()) {
            tts?.speak(speech, TextToSpeech.QUEUE_ADD, null, "textToSpeech")
        }
    }

    fun textToSpeechNoInterrupt(speech: String?) {
        if (ttsInit && !speech.isNullOrEmpty() && tts?.isSpeaking == false) {
            tts?.speak(speech, TextToSpeech.QUEUE_ADD, null, "textToSpeech")
        }
    }

    fun flush() {
        if (ttsInit) {
            tts?.stop()
        }
    }

    fun release() {
        soundPool.release()
        tts?.shutdown()
    }

    companion object {
        private const val NUMBER_OF_SIMULTANEOUS_SOUNDS = 4
        private const val LEFT_VOLUME_VALUE = 1.0f
        private const val RIGHT_VOLUME_VALUE = 1.0f
        private const val MUSIC_LOOP = 0
        private const val SOUND_PLAY_PRIORITY = 1
        private const val PLAY_RATE = 1.0f
    }
}
