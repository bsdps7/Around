package com.alefa.around.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class DebugRenderSystem extends EntitySystem implements Disposable {

    private World world;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    public DebugRenderSystem(World world, OrthographicCamera camera) {
        this.world = world;
        this.camera = camera;

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() { debugRenderer.dispose(); }

}
