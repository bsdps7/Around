package com.alefa.around.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by LEEFAMILY on 2018-03-25.
 */

public class StateComponent implements Component, Pool.Poolable {

    public static final int
            STATE_NORMAL = 0,
            STATE_MOVING = 1,
            STATE_DEAD = 2;

    private int state = 0;
    private float timer = 0;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        this.timer = 0;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    @Override
    public void reset() {
        state = 0;
        timer = 0;
    }
}
