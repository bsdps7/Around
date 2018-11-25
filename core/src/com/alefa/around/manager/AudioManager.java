package com.alefa.around.manager;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;

/**
 * A simple manager class for Sounds and Musics
 */

public class AudioManager implements Disposable {

    private static final String TAG = AudioManager.class.getSimpleName();

    /* -- Fields -- */
    private final Assets assets;
    private final PrefsManager prefsManager;

    private Music backgroundMusic;
    private boolean mute;
    private ArrayMap<String, Sound> gameplaySounds;

    private float volume = 1f;

    /* -- Constructor -- */
    public AudioManager(Assets assets, PrefsManager prefsManager) {
        this.assets = assets;
        this.prefsManager = prefsManager;
        this.mute = prefsManager.isMute();
    }

    /* -- Public methods -- */
    public void playBackgroundMusic() {

        if (mute)
            return;

        if (backgroundMusic == null) backgroundMusic = assets.getMusic(Assets.Audio.BG_MUSIC_2);

        backgroundMusic.setVolume(volume);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

    }

    public void pauseBackgroundMusic() {

        if (backgroundMusic == null) return;

        backgroundMusic.pause();

    }

    public void playGameplaySound(String sound) {

        if (mute)
            return;

        if (gameplaySounds == null) {
            gameplaySounds = new ArrayMap<String, Sound>();
        }

        if (gameplaySounds.get(sound) == null) gameplaySounds.put(sound, assets.getSound(sound));

        gameplaySounds.get(sound).play(volume);

    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
        prefsManager.setMute(mute);

        if (mute) {
            backgroundMusic.stop();
        } else {
            playBackgroundMusic();
        }
    }

    @Override
    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        Array<Sound> sounds = gameplaySounds.values().toArray();
        for (Sound sound : sounds) {
            sound.dispose();
        }
    }

}
