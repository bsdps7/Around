package com.alefa.around.component;

import com.alefa.around.utils.Constants;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class ObstacleComponent implements Component, Pool.Poolable {

    private boolean isHit = false;

    private float numSections = 1 / 2;

    private float angularSpeed = 0;

    private float radius = Constants.WORLD_WIDTH;

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public float getNumSections() {
        return numSections;
    }

    public void setNumSections(float numSections) {
        this.numSections = numSections;
    }

    public float getAngularSpeed() {
        return angularSpeed;
    }

    public void setAngularSpeed(float angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void reset() {
        isHit = false;
        numSections = 2;
        angularSpeed = 0;
        radius = Constants.WORLD_WIDTH;
    }
}
