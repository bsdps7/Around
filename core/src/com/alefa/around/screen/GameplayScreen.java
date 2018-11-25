package com.alefa.around.screen;

import com.alefa.around.AroundGame;
import com.alefa.around.event.EventQueue;
import com.alefa.around.event.GameEvent;
import com.alefa.around.manager.Assets;
import com.alefa.around.screen.overlay.GameoverOverlay;
import com.alefa.around.screen.overlay.PauseOverlay;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.GUIBuilder;
import com.alefa.around.world.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameplayScreen extends BaseScreen {

    private static final String TAG = GameplayScreen.class.getSimpleName();

    /* -- Fields -- */
    private GameWorld gameWorld;
    private EventQueue eventQueue;

    // ui
    private Image gemImage1, gemImage2;
    private Label gemsLabel, scoreLabel, plusGemsLabel;
    private Button pauseButton;

    /* -- Constructor -- */
    public GameplayScreen(AroundGame game) {
        super(game);

        gameWorld = game.getGameWorld();
        gameWorld.resumeLogic();
        eventQueue = new EventQueue();
        game.getGameEventSignal().add(eventQueue);
    }

    /* -- Public methods -- */
    @Override
    public void show() {
        super.show();

        inputMultiplexer.addProcessor(gameWorld);
        game.getGameEventSignal().dispatch(GameEvent.GAMEPLAY);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        gameWorld.update(delta);
        screenGUI.render();

        for (GameEvent gameEvent : eventQueue.getEvents()) {

            switch (gameEvent) {

                case GAMEOVER:
                    gameWorld.pauseLogic();
                    game.setScreen(new GameoverOverlay(game, this));
                    break;

                case SCORE:
                    break;

                case GEM:
                    plusGemsLabel.addAction(Actions.alpha(1f));
                    gemImage2.addAction(Actions.alpha(1f));
                    plusGemsLabel.setText("+" + (gameWorld.getTempScore() / 50 + 1));
                    plusGemsLabel.addAction(Actions.fadeOut(3f));
                    gemImage2.addAction(Actions.fadeOut(3f));
                    break;

                case PLAYER_DEAD:
                    Gdx.input.setInputProcessor(null);
                    break;

                default:
                    break;

            }

        }

        scoreLabel.setText(String.valueOf(gameWorld.getTempScore()));
        gemsLabel.setText(String.valueOf(game.getPrefsManager().getGems()));

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        gameWorld.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();

        gameWorld.pauseLogic();
    }

    @Override
    public void resume() {
        super.resume();

        game.setScreen(new PauseOverlay(game, this)); // automatically switches to the pause screen when focus is lost
    }

    @Override
    public void pauseLogic() {
        gameWorld.pauseLogic();
    }

    @Override
    public void resumeLogic() {
        gameWorld.resumeLogic();
    }

    @Override
    protected void initGUI() {

        // Initializing
        Table front = new Table();
        Table back = new Table();

        gemImage1 = GUIBuilder.createImage(assets, Assets.Regions.GUI_GEM);
        gemImage2 = GUIBuilder.createImage(assets, Assets.Regions.GUI_GEM);
            gemImage2.addAction(Actions.alpha(0));
        gemsLabel = GUIBuilder.createLabel(assets, String.valueOf(game.getPrefsManager().getGems()), Assets.Fonts.SIZE56);
        scoreLabel = GUIBuilder.createLabel(assets, String.valueOf(gameWorld.getTempScore()), Assets.Fonts.SIZE200);
        plusGemsLabel = GUIBuilder.createLabel(assets, "+1", Assets.Fonts.SIZE56);
            plusGemsLabel.addAction(Actions.alpha(0));
        pauseButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_PAUSE, Assets.Regions.GUI_BUTTON_PAUSE);
            pauseButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    gameWorld.pauseLogic();
                    game.setScreen(new PauseOverlay(game, GameplayScreen.this));
                }
            });

        // Layout

        front.top().add(scoreLabel).padTop(Constants.UI_UNIT * 2.25f).row();

        front.setFillParent(true);
        front.pack();

        back.pad(Constants.UI_UNIT / 2, Constants.UI_UNIT / 2, 0, Constants.UI_UNIT / 2);
        back.top().add(pauseButton).size(80f, 80f).left();
        back.add(gemsLabel).expandX().right();
        back.add(gemImage1).size(30f, 60f).padLeft(Constants.UI_UNIT / 4).row();
        back.add(plusGemsLabel).colspan(2).expandX().right();
        back.add(gemImage2).size(30f, 60f).padLeft(Constants.UI_UNIT / 4).row();

        back.setFillParent(true);
        back.pack();

        screenGUI.getFrontStage().addActor(front);
        screenGUI.getBackStage().addActor(back);

    }

}
