package com.alefa.around.screen.transition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseTransition {

    private final float duration;

    public BaseTransition(float duration) {
        this.duration = duration;
    }

    public float getDuration() {
        return duration;
    }

    public abstract void render(SpriteBatch spriteBatch, Texture currentScreenTexture, Texture nextScreenTexture, float percentage);

}
