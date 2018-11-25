package com.alefa.around.screen;

import com.alefa.around.AroundGame;
import com.alefa.around.manager.Assets;
import com.alefa.around.utils.GUIBuilder;
import com.alefa.around.world.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class LoadingScreen extends BaseScreen {

    private static final String TAG = LoadingScreen.class.getSimpleName();

    private float delayTime = 0.5f;   // seconds

    private Image splashLogo;

    public LoadingScreen(AroundGame game) {
        super(game);

        assets.loadLoading();
        assets.loadAll();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        screenGUI.render();

        Gdx.app.log(TAG, "renderUI: " + assets.getAssetManager().getProgress() + " - " + delta);

        if (assets.getAssetManager().update()) {    // update() returns true if all assets finish loading
            delayTime -= delta;

            if (delayTime <= 0) {
                game.setGameWorld(new GameWorld(game));
                game.getAudioManager().playBackgroundMusic();
                game.setScreen(new MenuScreen(game));
            }
        }
    }

    @Override
    protected void initGUI() {
        splashLogo = GUIBuilder.createImage(assets, Assets.Regions.SPLASH_LOGO);
        Table table = new Table();

        table.add(splashLogo).size(561f, 217f).expand().center().row();

        table.setFillParent(true);
        table.pack();

        screenGUI.getFrontStage().addActor(table);
    }
}
