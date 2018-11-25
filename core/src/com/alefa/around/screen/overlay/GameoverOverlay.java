package com.alefa.around.screen.overlay;

import com.alefa.around.AroundGame;
import com.alefa.around.manager.Assets;
import com.alefa.around.screen.BaseScreen;
import com.alefa.around.screen.MenuScreen;
import com.alefa.around.screen.transition.FadeScreenTransition;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.GUIBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameoverOverlay extends BaseOverlay {

    private static final String TAG = GameoverOverlay.class.getSimpleName();

    /* -- Fields -- */

    // ui
    private Image gemImage1, gemImage2;
    private Label scoreLabel, bestLabel, gemsLabel, tempGemsLabel, hintLabel, timerLabel;
    private Button giftButton;
    private Pixmap overlayPixmap;

    /* -- Constructor -- */
    public GameoverOverlay(AroundGame game, BaseScreen prevScreen) {
        super(game, prevScreen);

        disposePrevScreen = true;
    }

    /* -- Public methods -- */
    @Override
    public void render(float delta) {
        super.render(delta);

        screenGUI.render();
    }

    @Override
    public void show() {
        super.show();

        screenGUI.getFrontStage().addAction(Actions.alpha(0));
        screenGUI.getBackStage().addAction(Actions.alpha(0));
        screenGUI.getFrontStage().addAction(Actions.fadeIn(0.8f, Interpolation.fade));
        screenGUI.getBackStage().addAction(Actions.fadeIn(0.8f, Interpolation.fade));
    }

    @Override
    protected void initGUI() {

        // Initializing
        Table front = new Table();
        Table back  = new Table();

        gemImage1 = GUIBuilder.createImage(assets, Assets.Regions.GUI_GEM);
        gemImage2 = GUIBuilder.createImage(assets, Assets.Regions.GUI_GEM);
        if (game.getGameWorld().getTempGems() == 0) gemImage2.setDrawable(null);
        gemsLabel = GUIBuilder.createLabel(assets, String.valueOf(game.getPrefsManager().getGems()), Assets.Fonts.SIZE56);
        scoreLabel = GUIBuilder.createLabel(assets, String.valueOf(game.getGameWorld().getTempScore()), Assets.Fonts.SIZE200);
        bestLabel = GUIBuilder.createLabel(assets, "BEST " + game.getPrefsManager().getHighscore(), Assets.Fonts.SIZE84);
        if (game.getPrefsManager().setHighscore(game.getGameWorld().getTempScore())) bestLabel.setText("NEW BEST"); // setHighscore returns false if score is less than highscore
        timerLabel = GUIBuilder.createLabel(assets, "3:00", Assets.Fonts.SIZE56);
        timerLabel.setVisible(false);
        tempGemsLabel = GUIBuilder.createLabel(assets, "+" + game.getGameWorld().getTempGems(), Assets.Fonts.SIZE56);
        if (game.getGameWorld().getTempGems() == 0) tempGemsLabel.setText("");
        hintLabel = GUIBuilder.createLabel(assets, "TAP TO RESTART", Assets.Fonts.SIZE56);
        hintLabel.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1.5f), Actions.fadeIn(1.5f), Actions.delay(0.5f))));
        giftButton = GUIBuilder.createImageButton(assets, Assets.Regions.GUI_BUTTON_GIFT, Assets.Regions.GUI_BUTTON_GIFT);
        giftButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.setBubbles(false);

                return true;
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                recieveGift();
            }
        });
        giftButton.setTransform(true);
        giftButton.setOrigin(giftButton.getWidth() / 2, giftButton.getHeight() / 2);
        giftButton.addAction(Actions.forever(Actions.sequence(Actions.scaleTo(1.05f, 1.05f, 1f), Actions.scaleTo(1f, 1f, 1f))));
        overlayPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        overlayPixmap.setColor(new Color(0f,0f,0f,0.5f));
        overlayPixmap.fill();

        // Layout
        Table sup = new Table();
        sup.top().add(scoreLabel).padTop(Constants.UI_UNIT * 2.25f).row();
        sup.add(bestLabel).row();
        Table sub = new Table();
        sub.bottom().add(hintLabel).pad(0, 0, Constants.UI_UNIT * 4.75f, 0).colspan(2).row();

        front.add(sup).grow().uniformY().row();
        front.add(giftButton).size(160f).row();
        front.add(sub).grow().uniformY().row();

        front.setFillParent(true);
        front.pack();

        back.pad(Constants.UI_UNIT / 2, Constants.UI_UNIT / 2, 0, Constants.UI_UNIT / 2);
        back.top().add().size(80f).left();
        back.add(gemsLabel).expandX().right();
        back.add(gemImage1).padLeft(Constants.UI_UNIT / 4).size(30f, 60f).row();
        back.add(tempGemsLabel).colspan(2).expandX().right();
        back.add(gemImage2).padLeft(Constants.UI_UNIT / 4).size(30f, 60f).row();

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
                game.getGameWorld().reset();
                game.setScreen(new MenuScreen(game), new FadeScreenTransition(0.6f));
            }
        });

        screenGUI.getFrontStage().addActor(front);
        screenGUI.getBackStage().addActor(back);

    }

    /* -- Private methods -- */
    private void recieveGift() {
        Gdx.app.log(TAG, "recieveGift: ");
    }

}
