package com.alefa.around;

import com.alefa.around.event.GameEvent;
import com.alefa.around.manager.Assets;
import com.alefa.around.manager.AudioManager;
import com.alefa.around.manager.PrefsManager;
import com.alefa.around.screen.BaseScreen;
import com.alefa.around.screen.LoadingScreen;
import com.alefa.around.screen.overlay.BaseOverlay;
import com.alefa.around.screen.transition.BaseTransition;
import com.alefa.around.world.GameWorld;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** The main game class.
 * Doesn't inherit Game class to operate screen handling manually, and be able to have screen transitions.
 * @author LEEFAMILY */

public class AroundGame extends ApplicationAdapter {

	private static final String TAG = AroundGame.class.getSimpleName();

	private final Assets assets;

	private GameWorld gameWorld;
	private PrefsManager prefsManager;
	private AudioManager audioManager;
	private Signal<GameEvent> gameEventSignal;

	// Transitions
	private Viewport transitionViewport;
	private SpriteBatch transitionBatch;

	private BaseTransition screenTransition;
	private BaseScreen currentScreen, nextScreen;
	private FrameBuffer currentFrameBuffer, nextFrameBuffer;

	private float transitionTime;
	private boolean renderedToTexture, transitionInProgress;

	public AroundGame() {
		this.assets = new Assets();
	}

	@Override
	public void create () {
		prefsManager = new PrefsManager();
		audioManager = new AudioManager(assets, prefsManager);
		gameEventSignal = new Signal<GameEvent>();

		transitionViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		transitionBatch = new SpriteBatch();

		// change to LoadingScreen
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		if (nextScreen == null) { // no transition is in progress
			if (currentScreen != null) {
				currentScreen.render(delta);
			}
		} else {
			transitionInProgress = true;
			transitionTime = Math.min(transitionTime + delta, getTransitionDuration());

			// render to texture only once for performance
			if (!renderedToTexture) {
				renderScreensToTexture();
				renderedToTexture = true;
			}

			// update transitions
			updateTransitions();
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(TAG, "resize: " + width + " x " + height);

		if (currentScreen != null) {
			currentScreen.resize(width, height);
		}

		if (nextScreen != null) {
			nextScreen.resize(width, height);
		}

		transitionViewport.update(width, height, true);
	}

	@Override
	public void pause() {
		Gdx.app.log(TAG, "pause: ");
//		prefsManager.save();

		if (currentScreen != null) {
			currentScreen.pause();
		}
	}

	@Override
	public void resume() {
		Gdx.app.log(TAG, "resume: ");
//		prefsManager.load();

		if (currentScreen != null) {
			currentScreen.resume();
		}
	}

	/** Set screen without a transition */
	public void setScreen(BaseScreen screen) {
		setScreen(screen, null);
	}

	/** Manual setting of screen, with transition */
	public void setScreen(BaseScreen screen, BaseTransition transition) {
		if (transitionInProgress || currentScreen == screen) return;

		// disable input processors during screen transition
		Gdx.input.setInputProcessor(null);

		this.screenTransition = screenTransition;

		// creating frame buffers
		int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight();

		currentFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false); // depth is for 3-dimensional frame buffers
		nextFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

		if (currentScreen != null) currentScreen.pauseLogic();

		nextScreen = screen;
		nextScreen.show();
		nextScreen.resize(width, height);
		nextScreen.pauseLogic();

		transitionTime = 0;
	}

	@Override
	public void dispose () {
		if (currentScreen != null) currentScreen.dispose();
		if (nextScreen != null) nextScreen.dispose();

		if (currentFrameBuffer != null) currentFrameBuffer.dispose();
		if (nextFrameBuffer != null) nextFrameBuffer.dispose();

		assets.dispose();
//		audioManager.dispose();

		currentScreen = null;
		nextScreen = null;
	}

	private float getTransitionDuration() {
		return screenTransition == null ? 0f : screenTransition.getDuration(); // return 0 if transition is null
	}

	/** Render the screens to a FrameBuffer object, in order to store them as textures */
	private void renderScreensToTexture() {
		if (currentScreen != null) {
			currentFrameBuffer.begin();
			currentScreen.render(0); // 0 to render first frame
			currentFrameBuffer.end();
		}

		if (nextScreen != null) {
			nextFrameBuffer.begin();
			nextScreen.render(0);
			nextFrameBuffer.end();
		}
	}

	private void updateTransitions() {
		if (screenTransition == null || transitionTime >= getTransitionDuration()) {        // transition just finished or no transition is specified

			if (currentScreen != null) {
				currentScreen.hide();
				if (nextScreen != null && !(nextScreen instanceof BaseOverlay))
					currentScreen.dispose(); // only dispose the current screen if next screen is not an overlay
			}

			// resume next screen
			if (nextScreen != null) {
				nextScreen.resumeLogic();
				Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
			}

			// switch screens and reset
			currentScreen = nextScreen;
			nextScreen = null;
			screenTransition = null;
			currentFrameBuffer.dispose();
			nextFrameBuffer.dispose();
			currentFrameBuffer = null;
			nextFrameBuffer = null;
			renderedToTexture = false;
			transitionInProgress = false;
			return;

		}

		float percentage = transitionTime / getTransitionDuration();
		Gdx.app.log(TAG, "updateTransitions: " + percentage);

		// the textures are auto disposed when the frame buffer objects are disposed
		Texture currentScreenTexture = currentFrameBuffer.getColorBufferTexture(), nextScreenTexture = nextFrameBuffer.getColorBufferTexture();

		// render transition to screen
		transitionBatch.setProjectionMatrix(transitionViewport.getCamera().combined);
		screenTransition.render(transitionBatch, currentScreenTexture, nextScreenTexture, percentage);
	}

	public Assets getAssets() {
		return assets;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}

	public PrefsManager getPrefsManager() {
		return prefsManager;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public Signal<GameEvent> getGameEventSignal() {
		return gameEventSignal;
	}
}
