package com.alefa.around.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by LEEFAMILY on 2018-03-26.
 */

public class TypeComponent implements Component, Pool.Poolable {

    public static final int
            TYPE_FLOOR = 0,
            TYPE_PLAYER = 1,
            TYPE_OBSTACLE = 2;

    private int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void reset() {
        type = 0;
    }
}
