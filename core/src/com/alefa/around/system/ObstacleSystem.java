package com.alefa.around.system;

import com.alefa.around.component.BodyComponent;
import com.alefa.around.component.ObstacleComponent;
import com.alefa.around.component.RenderComponent;
import com.alefa.around.component.StateComponent;
import com.alefa.around.utils.ComponentMappers;
import com.alefa.around.utils.Constants;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class ObstacleSystem extends IteratingSystem {

    private static final String TAG = ObstacleSystem.class.getSimpleName();

    /* -- Constants -- */
    private static final Family FAMILY = Family.all(
            ObstacleComponent.class,
            BodyComponent.class,
            StateComponent.class
    ).get();

    /* -- Constructor -- */
    public ObstacleSystem() {
        super(FAMILY);
    }

    /* -- Public methods -- */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        StateComponent stateComponent = ComponentMappers.STATE_COMPONENT.get(entity);
        stateComponent.setState(StateComponent.STATE_MOVING);

        BodyComponent bodyComponent = ComponentMappers.BODY_COMPONENT.get(entity);
        Body body = bodyComponent.getBody();

        ObstacleComponent obstacleComponent = ComponentMappers.OBSTACLE_COMPONENT.get(entity);
        float radius = obstacleComponent.getRadius() + Constants.OBSTACLE_SPEED * deltaTime;
        float numSections = obstacleComponent.getNumSections();

        body.destroyFixture(body.getFixtureList().first());

        FixtureDef fixtureDef = new FixtureDef();
        ChainShape shape = new ChainShape();
        Vector2[] vertices = new Vector2[Constants.CIRCLE_VERTICES_NUM];
        for (int i = 0; i < Constants.CIRCLE_VERTICES_NUM; i++) {

            float x = MathUtils.cosDeg(i * (360f - 360f / numSections) / Constants.CIRCLE_VERTICES_NUM) * radius;
            float y = MathUtils.sinDeg(i * (360f - 360f / numSections) / Constants.CIRCLE_VERTICES_NUM) * radius;

            vertices[i] = new Vector2(x, y);

        }
        shape.createChain(vertices);
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BITS.OBSTACLE;
        fixtureDef.filter.maskBits = Constants.MASK_BITS.OBSTACLE;
        body.createFixture(fixtureDef);

        obstacleComponent.setRadius(radius);

        RenderComponent renderComponent = ComponentMappers.RENDER_COMPONENT.get(entity);
        if (renderComponent != null) {
            renderComponent.setWidth(radius * 2);
            renderComponent.setHeight(radius * 2);
        }

    }

}
