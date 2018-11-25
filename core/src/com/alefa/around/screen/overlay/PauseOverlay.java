package com.alefa.around.screen.overlay;

import com.alefa.around.AroundGame;
import com.alefa.around.event.GameEvent;
import com.alefa.around.manager.Assets;
import com.alefa.around.screen.BaseScreen;
import com.alefa.around.screen.GameplayScreen;
import com.alefa.around.utils.GUIBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseOverlay extends BaseOverlay {

    /* -- Fields -- */

    // ui
    private Button resumeButton;
    private Pixmap overlayPixmap;

    /* -- Constructor -- */
    public PauseOverlay(AroundGame game, BaseScreen prevScreen) {
        super(game, prevScreen);

        disposePrevScreen = false;
    }

    /* -- Public methods -- */
    @Override
    public void show() {
        super.show();

        game.getAudioManager().pauseBackgroundMusic();
        game.getGameEventSignal().dispatch(GameEvent.PAUSE);
    }

    @Override
    public void hide() {
        super.hide();

        game.getAudioManager().playBackgroundMusic();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        screenGUI.render();
    }

    @Override
    protected void initGUI() {

        // Initializing
        Table back  = new Table();

        resumeButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_RESUME, Assets.Regions.GUI_BUTTON_RESUME);
        overlayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        overlayPixmap.setColor(new Color(0f,0f,0f,0.5f));
        overlayPixmap.fill();

        // Layout
        back.add(resumeButton).size(160f).expand().center();

        back.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(overlayPixmap))));
        back.setFillParent(true);
        back.pack();
        back.setTouchable(Touchable.enabled);
        back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameplayScreen(game));
            }
        });

        screenGUI.getBackStage().addActor(back);

    }

}
