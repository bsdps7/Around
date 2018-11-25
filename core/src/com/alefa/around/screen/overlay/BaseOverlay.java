package com.alefa.around.screen.overlay;

import com.alefa.around.AroundGame;
import com.alefa.around.screen.BaseScreen;

public abstract class BaseOverlay extends BaseScreen {

    /* -- Fields -- */
    protected final BaseScreen prevScreen;
    protected boolean disposePrevScreen = false; // Whether to dispose the previous prevScreen on hide

    /* -- Constructor -- */
    public BaseOverlay(AroundGame game, BaseScreen prevScreen) {
        super(game);

        this.prevScreen = prevScreen;
    }

    /* -- Public methods -- */
    @Override
    public void render(float delta) {
        super.render(delta);

        prevScreen.render(delta);   // render the previous screen
    }

    @Override
    public void dispose() {
        super.dispose();

        if (disposePrevScreen) {
            prevScreen.dispose();
        }
    }
}
