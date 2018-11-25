package com.alefa.around.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by LEEFAMILY on 2018-03-26.
 */

public class CollisionComponent implements Component, Pool.Poolable {

    private Entity collidedEntity = null;

    public Entity getCollidedEntity() {
        return collidedEntity;
    }

    public void setCollidedEntity(Entity collidedEntity) {
        this.collidedEntity = collidedEntity;
    }

    @Override
    public void reset() {
        collidedEntity = null;
    }
}
