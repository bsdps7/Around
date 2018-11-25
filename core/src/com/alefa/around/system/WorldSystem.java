package com.alefa.around.system;

import com.alefa.around.component.BodyComponent;
import com.alefa.around.component.StateComponent;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.ComponentMappers;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class WorldSystem extends IteratingSystem {

    private static final String TAG = WorldSystem.class.getSimpleName();

    /* -- Constants -- */
    private static final Family FAMILY = Family.all(
            BodyComponent.class,
            StateComponent.class
    ).get();

    /* -- Fields -- */
    private World world;
    private Array<Entity> entitiesQueue;

    private float accumulator = 0f;

    /* -- Constructor -- */
    public WorldSystem(World world) {
        super(FAMILY);

        this.world = world;
        this.entitiesQueue = new Array<Entity>();
    }

    /* -- Public methods -- */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accumulator += Math.min(deltaTime, 0.25f);
        if (accumulator >= 1 / Constants.BOX2D_FPS) {

            world.step(1 / Constants.BOX2D_FPS, Constants.BOX2D_VELOCITY_ITERATIONS, Constants.BOX2D_POSITION_ITERATIONS);

            accumulator -= 1 / Constants.BOX2D_FPS;

            for (Entity entity : entitiesQueue) {

                StateComponent stateComponent = ComponentMappers.STATE_COMPONENT.get(entity);
                int state = stateComponent.getState();

                BodyComponent bodyComponent = ComponentMappers.BODY_COMPONENT.get(entity);
                Body body = bodyComponent.getBody();

                if (state == StateComponent.STATE_DEAD) {

                    world.destroyBody(body);
                    getEngine().removeEntity(entity);
                }
            }
        }

        entitiesQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entitiesQueue.add(entity);
    }
}
