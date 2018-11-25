package com.alefa.around.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class BodyComponent implements Component {

    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
