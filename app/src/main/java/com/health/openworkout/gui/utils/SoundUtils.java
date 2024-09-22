package com.health.openworkout.gui.utils;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.speech.tts.TextToSpeech;

import java.io.IOException;
import java.util.Locale;

import timber.log.Timber;

public class SoundUtils {
    public enum SOUND {WORKOUT_COUNT_BEFORE_START, WORKOUT_START, WORKOUT_STOP, SESSION_COMPLETED}

    private final int NUMBER_OF_SIMULTANEOUS_SOUNDS = 4;
    private final float LEFT_VOLUME_VALUE = 1.0f;
    private final float RIGHT_VOLUME_VALUE = 1.0f;
    private final int MUSIC_LOOP = 0;
    private final int SOUND_PLAY_PRIORITY = 1;
    private final float PLAY_RATE= 1.0f;

    private SoundPool soundPool;
    private TextToSpeech ttS;
    private boolean ttsInit;

    private int soundIdBeforeStart, soundIdWorkoutStart, soundIdWorkoutStop, soundIdSessionCompleted;

    private AssetManager assetManager;

    public SoundUtils(Context context) {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(NUMBER_OF_SIMULTANEOUS_SOUNDS)
                .build();
        assetManager = context.getAssets();
        ttsInit = false;

        ttS = new TextToSpeech(context, status -> {

            if (status == TextToSpeech.ERROR) {
                Timber.e("Can't initialize text to speech");
            }

            if (status == TextToSpeech.SUCCESS) {
                ttS.setLanguage(Locale.ENGLISH);
                ttsInit = true;
            }
        });

        loadSounds();
    }

    private void loadSounds() {
        try {
            AssetFileDescriptor afd;

            afd = assetManager.openFd("sound/workout.mp3");
            soundIdBeforeStart = soundPool.load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(),1);
            afd = assetManager.openFd("sound/workout_start.mp3");
            soundIdWorkoutStart = soundPool.load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(),1);
            afd = assetManager.openFd("sound/workout_stop.mp3");
            soundIdWorkoutStop = soundPool.load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(),1);
            afd = assetManager.openFd("sound/session_completed.mp3");
            soundIdSessionCompleted = soundPool.load(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength(),1);
        } catch (IOException ex) {
            Timber.e(ex);
        }
    }

    public void playSound(SOUND sound) {
        switch (sound) {
            case WORKOUT_COUNT_BEFORE_START:
                soundPool.play(soundIdBeforeStart, LEFT_VOLUME_VALUE , RIGHT_VOLUME_VALUE, SOUND_PLAY_PRIORITY , MUSIC_LOOP ,PLAY_RATE);
                break;
            case WORKOUT_START:
                soundPool.play(soundIdWorkoutStart, LEFT_VOLUME_VALUE , RIGHT_VOLUME_VALUE, SOUND_PLAY_PRIORITY , MUSIC_LOOP ,PLAY_RATE);
                break;
            case WORKOUT_STOP:
                soundPool.play(soundIdWorkoutStop, LEFT_VOLUME_VALUE , RIGHT_VOLUME_VALUE, SOUND_PLAY_PRIORITY , MUSIC_LOOP ,PLAY_RATE);
                break;
            case SESSION_COMPLETED:
                soundPool.play(soundIdSessionCompleted, LEFT_VOLUME_VALUE , RIGHT_VOLUME_VALUE, SOUND_PLAY_PRIORITY , MUSIC_LOOP ,PLAY_RATE);
                break;
        }
    }

    public void textToSpeech(final String speech) {
        if (ttsInit) {
            ttS.speak(speech, TextToSpeech.QUEUE_ADD, null, "textToSpeech");
        }
    }

    public void textToSpeechNoInterrupt(final String speech) {
        if (ttsInit) {
            if (!ttS.isSpeaking()) {
                ttS.speak(speech, TextToSpeech.QUEUE_ADD, null, "textToSpeech");
            }
        }
    }

    public void flush() {
        if (ttsInit) {
            ttS.stop();
        }
    }

    public void release() {
        soundPool.release();
        ttS.shutdown();
    }
}
