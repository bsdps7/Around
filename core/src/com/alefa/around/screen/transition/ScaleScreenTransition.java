package com.alefa.around.screen.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

public class ScaleScreenTransition extends BaseTransition {

    /* -- Fields -- */
    private boolean scaleOut = false;
    private Interpolation interpolation;

    public ScaleScreenTransition(float duration, boolean scaleOut) {
        super(duration);
        this.scaleOut = scaleOut;
        this.interpolation = Interpolation.fade;
    }

    public ScaleScreenTransition(float duration, boolean scaleOut, Interpolation interpolation) {
        super(duration);
        this.scaleOut = scaleOut;
        this.interpolation = interpolation;
    }

    @Override
    public void render(SpriteBatch spriteBatch, Texture currentScreenTexture, Texture nextScreenTexture, float percentage) {

        Texture bottomScreenTexture, topScreenTexture;
        int bottomScreenTextureWidth, bottomScreenTextureHeight;
        int topScreenTextureWidth, topScreenTextureHeight;

        if (scaleOut) { // next screen texture scales out
            bottomScreenTexture = currentScreenTexture;
            topScreenTexture = nextScreenTexture;
        } else { // current screen texture scales in
            bottomScreenTexture = nextScreenTexture;
            topScreenTexture = currentScreenTexture;
        }
        bottomScreenTextureWidth = bottomScreenTexture.getWidth();
        bottomScreenTextureHeight = bottomScreenTexture.getHeight();
        topScreenTextureWidth = topScreenTexture.getWidth();
        topScreenTextureHeight = topScreenTexture.getHeight();

        // interpolate percentage
        percentage = interpolation.apply(percentage);

        float scale = scaleOut ? percentage : 1 - percentage;

        // clear screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        // draw bottom screen texture
        spriteBatch.draw(bottomScreenTexture,
                0, 0,
                0, 0,
                bottomScreenTextureWidth, bottomScreenTextureHeight, // width, height
                1, 1,
                0,
                0, 0,
                bottomScreenTextureWidth, bottomScreenTextureHeight, // src width, src height
                false, true // y needs to be flipped as y space is different between texture renderring and frame buffers
        );

        // draw top screen texture
        spriteBatch.draw(topScreenTexture,
                0, 0,
                topScreenTextureWidth / 2, topScreenTextureHeight / 2, // set scale origin to center of texture
                topScreenTextureWidth, topScreenTextureHeight, // width, height
                scale, scale,
                0,
                0, 0,
                topScreenTextureWidth, topScreenTextureHeight, // src width, src height
                false, true // y needs to be flipped as y space is different between texture renderring and frame buffers
        );

        spriteBatch.end();

    }

}
