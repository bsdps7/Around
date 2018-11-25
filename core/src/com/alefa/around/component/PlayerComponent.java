package com.alefa.around.component;

import com.badlogic.ashley.core.Component;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class PlayerComponent implements Component {

    private float anglePosDeg = 0;

    public float getAnglePosDeg() {
        return anglePosDeg;
    }

    public void setAnglePosDeg(float anglePosDeg) {
        this.anglePosDeg = anglePosDeg;
    }
}
