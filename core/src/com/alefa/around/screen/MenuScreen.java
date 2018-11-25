package com.alefa.around.screen;

import com.alefa.around.AroundGame;
import com.alefa.around.event.GameEvent;
import com.alefa.around.manager.Assets;
import com.alefa.around.screen.transition.FadeScreenTransition;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.GUIBuilder;
import com.alefa.around.world.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuScreen extends BaseScreen {

    private static final String TAG = MenuScreen.class.getSimpleName();

    /* -- Fields -- */
    private GameWorld gameWorld;

    // ui
    private Image gemImage, titleImage;
    private Label gemsLabel, hintLabel;
    private Button helpButton, shopButton, soundButton;

    /* -- Constructor -- */
    public MenuScreen(AroundGame game) {
        super(game);

        gameWorld = game.getGameWorld();
        gameWorld.pauseLogic(); // the GameWorld continues rendering, but the logic does not update
    }

    /* -- Public methods -- */
    @Override
    public void show() {
        super.show();

        game.getGameEventSignal().dispatch(GameEvent.MENU);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        gameWorld.update(delta);
        screenGUI.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        gameWorld.resize(width, height);
    }

    @Override
    protected void initGUI() {

        // Initializing
        Table front = new Table();
        Table back = new Table();

        gemImage = GUIBuilder.createImage(assets, Assets.Regions.GUI_GEM);
        titleImage = GUIBuilder.createImage(assets, Assets.Regions.TITLE_MAIN);
        gemsLabel = GUIBuilder.createLabel(assets, String.valueOf(game.getPrefsManager().getGems()), Assets.Fonts.SIZE56);
        hintLabel = GUIBuilder.createLabel(assets,"TAP TO START", Assets.Fonts.SIZE56);
        hintLabel.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(2f), Actions.fadeIn(2f))));
        helpButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_HELP, Assets.Regions.GUI_BUTTON_HELP);
        helpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.setBubbles(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touchUp: HELP");
            }
        });
        shopButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_SHOP, Assets.Regions.GUI_BUTTON_SHOP);
        shopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.setBubbles(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "touchUp: SHOP");
            }
        });
        if (!game.getAudioManager().isMute()) {
            soundButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_SOUND_OFF, Assets.Regions.GUI_BUTTON_SOUND_OFF);
        } else {
            soundButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_SOUND_ON, Assets.Regions.GUI_BUTTON_SOUND_ON);
        }
        soundButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.setBubbles(false);
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!game.getAudioManager().isMute()) {
                    game.getAudioManager().setMute(true);
                    GUIBuilder.changeImageButton(assets, soundButton, Assets.Regions.GUI_BUTTON_SOUND_ON, Assets.Regions.GUI_BUTTON_SOUND_ON);
                } else {
                    game.getAudioManager().setMute(false);
                    GUIBuilder.changeImageButton(assets, soundButton, Assets.Regions.GUI_BUTTON_SOUND_OFF, Assets.Regions.GUI_BUTTON_SOUND_OFF);
                }
            }
        });

        // Layout

        Table sub = new Table();
        sub.pad(0, Constants.UI_UNIT / 2, Constants.UI_UNIT * 2f, Constants.UI_UNIT / 2);
        sub.bottom().add(hintLabel).padBottom(Constants.UI_UNIT).colspan(3).row();
        sub.add(helpButton).size(140f).expandX().center();
        sub.add(shopButton).size(140f).expandX().center();
        sub.add(soundButton).size(140f).expandX().center();

        front.add(titleImage).size(510f, 140f).padTop(Constants.UI_UNIT * 2.5f).row();
        front.add(sub).grow();

        front.setFillParent(true);
        front.pack();
        front.setTouchable(Touchable.enabled);
        front.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameplayScreen(game), new FadeScreenTransition(0.7f));
            }
        });

        back.pad(Constants.UI_UNIT / 2, Constants.UI_UNIT / 2, 0, Constants.UI_UNIT / 2);
        back.top().add().size(80f);
        back.add(gemsLabel).expandX().right();
        back.add(gemImage).size(30f, 60f).padLeft(Constants.UI_UNIT / 4);

        back.setFillParent(true);
        back.pack();

        screenGUI.getFrontStage().addActor(front);
        screenGUI.getBackStage().addActor(back);

    }
}
