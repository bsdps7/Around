package com.alefa.around.utils;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** A utility class that contains code for a screen's GUI.
 * @author LEEFAMILY */

public class ScreenGUI implements Disposable {

    /* Two layers of GUI for more flexibility in design */
    private Viewport backViewport, frontViewport;
    private Stage backStage, frontStage;

    public ScreenGUI() {
        backViewport = new ExtendViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);
        frontViewport = new ExtendViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);

        backStage = new Stage(backViewport);
        frontStage = new Stage(frontViewport);
    }

    public void render() {
        backViewport.apply();
        backStage.act();
        backStage.draw();

        frontViewport.apply();
        frontStage.act();
        frontStage.draw();
    }

    public void resize(int width, int height) {
        backViewport.update(width, height, true);
        frontViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        backStage.dispose();
        frontStage.dispose();
    }

    public Stage getBackStage() {
        return backStage;
    }

    public Stage getFrontStage() {
        return frontStage;
    }

}
