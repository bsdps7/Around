package com.alefa.around.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefsManager {

    private static final String TAG = PrefsManager.class.getSimpleName();

    /* -- Constants -- */
    private static final String PREFS_KEY = "com.aleph.tau";
    private static final String
            PREFS_HIGHSCORE_KEY = "highscore",
            PREFS_GEMS_KEY = "gems",
            PREFS_MUTE_KEY = "mute";

    /* -- Fields -- */
    private Preferences preferences;

    private int highscore = 0;
    private int gems = 0;
    private boolean mute = false;

    /* -- Constructor -- */
    public PrefsManager() {
        preferences = Gdx.app.getPreferences(PREFS_KEY);

        load();
    }

    /* -- Public methods -- */
    public void save() {

        preferences.putInteger(PREFS_HIGHSCORE_KEY, highscore);
        preferences.putInteger(PREFS_GEMS_KEY, gems);
        preferences.putBoolean(PREFS_MUTE_KEY, mute);

        preferences.flush();

    }

    public void load() {

        if (preferences == null) {
            preferences = Gdx.app.getPreferences(PREFS_KEY);
        }

        highscore = preferences.getInteger(PREFS_HIGHSCORE_KEY, 0);
        gems = preferences.getInteger(PREFS_GEMS_KEY, 0);
        mute = preferences.getBoolean(PREFS_MUTE_KEY, false);
    }

    /* -- Getters and Setters -- */
    public int getHighscore() {
        return highscore;
    }

    public boolean setHighscore(int highscore) {
        if (highscore <= this.highscore)
            return false;

        this.highscore = highscore;

        return true;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
