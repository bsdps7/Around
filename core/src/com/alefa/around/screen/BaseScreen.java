package com.alefa.around.screen;

import com.alefa.around.AroundGame;
import com.alefa.around.manager.Assets;
import com.alefa.around.utils.ScreenGUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/** The abstract base class for all screens to inherit.
 * Handles the clearing, resizing, input processing, and other basic logic of screens.
 * @author LEEFAMILY */

public abstract class BaseScreen implements Screen {

    private static final String TAG = BaseScreen.class.getSimpleName();

    protected final AroundGame game;
    protected final Assets assets;
    protected ScreenGUI screenGUI;
    protected InputMultiplexer inputMultiplexer;

    public BaseScreen(AroundGame game) {
        this.game = game;
        assets = game.getAssets();
    }

    @Override
    public void show() {
        Gdx.app.log(TAG, "show: " + this.getClass().getSimpleName());

        screenGUI = new ScreenGUI();
        inputMultiplexer = new InputMultiplexer(screenGUI.getBackStage(), screenGUI.getFrontStage());

        initGUI();

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log(TAG, "resize: " + this.getClass().getSimpleName());

        screenGUI.resize(width, height);
    }

    @Override
    public void pause() {
        Gdx.app.log(TAG, "pause: " + this.getClass().getSimpleName());
    }

    @Override
    public void resume() {
        Gdx.app.log(TAG, "resume: " + this.getClass().getSimpleName());
    }

    @Override
    public void hide() {
        Gdx.app.log(TAG, "hide: " + this.getClass().getSimpleName());

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.app.log(TAG, "dispose: " + this.getClass().getSimpleName());

        screenGUI.dispose();
    }

    /** Pauses and resumes the game logic (rendering is ignored) */
    public void pauseLogic() {}
    public void resumeLogic() {}

    protected abstract void initGUI();

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }
}
