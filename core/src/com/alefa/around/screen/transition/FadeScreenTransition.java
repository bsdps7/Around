package com.alefa.around.screen.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class FadeScreenTransition extends BaseTransition {

    public FadeScreenTransition(float duration) {
        super(duration);
    }

    @Override
    public void render(SpriteBatch spriteBatch, Texture currentScreenTexture, Texture nextScreenTexture, float percentage) {

        int currentScreenTextureWidth = currentScreenTexture.getWidth(), currentScreenTextureHeight = currentScreenTexture.getHeight();
        int nextScreenTextureWidth = nextScreenTexture.getWidth(), nextScreenTextureHeight = nextScreenTexture.getHeight();

        // interpolate percentage, using the type fade for non-linear, smoothed out at the ends effect
        percentage = Interpolation.fade.apply(percentage);

        // clear screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Color oldColor = spriteBatch.getColor().cpy();

        spriteBatch.begin();

        // draw current screen
        spriteBatch.setColor(1, 1, 1, 1 - percentage); // white color with transparency, alpha decreasing
        spriteBatch.draw(currentScreenTexture,
                0, 0,
                0, 0,
                currentScreenTextureWidth, currentScreenTextureHeight, // width, height
                1, 1,
                0,
                0, 0,
                currentScreenTextureWidth, currentScreenTextureHeight, // src width, src height
                false, true // y needs to be flipped as y space is different between texture renderring and frame buffers
        );

        // draw next screen
        spriteBatch.setColor(1, 1, 1, percentage); // white color with transparency, alpha increasing
        spriteBatch.draw(nextScreenTexture,
                0, 0,
                0, 0,
                nextScreenTextureWidth, nextScreenTextureHeight, // width, height
                1, 1,
                0,
                0, 0,
                nextScreenTextureWidth, nextScreenTextureHeight, // src width, src height
                false, true // y needs to be flipped as y space is different between texture renderring and frame buffers
        );

        spriteBatch.setColor(oldColor);
        spriteBatch.end();

    }

}
