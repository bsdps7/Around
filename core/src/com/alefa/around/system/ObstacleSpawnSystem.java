package com.alefa.around.system;

import com.alefa.around.utils.Constants;
import com.alefa.around.utils.EntityFactory;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class ObstacleSpawnSystem extends IntervalSystem {

    private static final String TAG = ObstacleSpawnSystem.class.getSimpleName();

    /* -- Fields -- */
    private EntityFactory entityFactory;

    /* -- Constructor -- */
    public ObstacleSpawnSystem(EntityFactory entityFactory) {
        super(Constants.OBSTACLE_SPAWN_INTERVAL);
        this.entityFactory = entityFactory;
    }

    /* -- Public methods -- */
    @Override
    public void addedToEngine(Engine engine) {

    }

    @Override
    protected void updateInterval() {

        int type = MathUtils.random(4);
        float angularSpeed = MathUtils.randomSign() * 180;
        float angle = MathUtils.randomSign() * 360;

        entityFactory.createObstacle(type, angularSpeed, angle);
    }

}
