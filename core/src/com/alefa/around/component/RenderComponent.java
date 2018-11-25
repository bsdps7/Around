package com.alefa.around.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by LEEFAMILY on 2018-03-26.
 */

public class RenderComponent implements Component, Pool.Poolable {

    private TextureRegion region = null;

    private float width = 0, height = 0;

    private float z = 0;

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public void reset() {
        region = null;
        width = 0;
        height = 0;
    }
}
