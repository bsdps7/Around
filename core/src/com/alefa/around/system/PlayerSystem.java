package com.alefa.around.system;

import com.alefa.around.component.BodyComponent;
import com.alefa.around.component.PlayerComponent;
import com.alefa.around.component.StateComponent;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.ComponentMappers;
import com.alefa.around.world.GameWorld;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class PlayerSystem extends IteratingSystem {

    private static final String TAG = PlayerSystem.class.getSimpleName();

    /* -- Constants -- */
    private static final Family FAMILY = Family.all(
            PlayerComponent.class,
            BodyComponent.class,
            StateComponent.class
    ).get();

    /* -- Fields -- */
    private GameWorld gameWorld;

    /* -- Constructor -- */
    public PlayerSystem(GameWorld gameWorld) {
        super(FAMILY);

        this.gameWorld = gameWorld;
    }

    /* -- Public methods -- */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if (gameWorld.isRightTouched() || gameWorld.isLeftTouched()) {

            StateComponent stateComponent = ComponentMappers.STATE_COMPONENT.get(entity);
            stateComponent.setState(StateComponent.STATE_MOVING);

            BodyComponent bodyComponent = ComponentMappers.BODY_COMPONENT.get(entity);
            Body body = bodyComponent.getBody();

            PlayerComponent playerComponent = ComponentMappers.PLAYER_COMPONENT.get(entity);
            float angle = playerComponent.getAnglePosDeg();

            if (gameWorld.isRightTouched()) {
                angle -= Constants.PLAYER_ANGLE_SPEED_DEG * deltaTime; // clockwise - negative theta
            }
            if (gameWorld.isLeftTouched()) {
                angle += Constants.PLAYER_ANGLE_SPEED_DEG * deltaTime; // counterclockwise - positive theta
            }

            playerComponent.setAnglePosDeg(angle);

            body.setTransform(Constants.WORLD_WIDTH / 2 + MathUtils.cosDeg(angle) * (Constants.PLAYER_PATH_RADIUS),
                    Constants.WORLD_HEIGHT / 2 + MathUtils.sinDeg(angle) * (Constants.PLAYER_PATH_RADIUS),
                    MathUtils.degRad * (angle - Constants.PLAYER_INIT_ANGLE_DEG));

        }

    }

}
