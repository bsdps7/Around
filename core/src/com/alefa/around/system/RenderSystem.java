package com.alefa.around.system;

import com.alefa.around.component.BodyComponent;
import com.alefa.around.component.RenderComponent;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.ComponentMappers;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Comparator;

/**
 * Created by LEEFAMILY on 2018-03-26.
 */

public class RenderSystem extends SortedIteratingSystem implements Disposable {

    private static final String TAG = RenderSystem.class.getSimpleName();

    /* -- Constants -- */
    private static final Family FAMILY = Family.all(
            RenderComponent.class
    ).get();

    /* -- Fields -- */
    private SpriteBatch spriteBatch;
    private Viewport viewport;

    private float tint = 0;

    /* -- Constructor -- */
    public RenderSystem(Viewport viewport, SpriteBatch spriteBatch) {
        super(FAMILY, new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {

                float zA = ComponentMappers.RENDER_COMPONENT.get(entityA).getZ();
                float zB = ComponentMappers.RENDER_COMPONENT.get(entityB).getZ();

                return (int) Math.signum(zA - zB); // returns 1 if positive, 0 if equal, -1 if negative
            }
        });

        this.spriteBatch = spriteBatch;
        this.viewport = viewport;
    }

    /* -- Public methods -- */
    @Override
    public void update(float deltaTime) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        super.update(deltaTime);

        spriteBatch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        BodyComponent bodyComponent = ComponentMappers.BODY_COMPONENT.get(entity);
        RenderComponent renderComponent = ComponentMappers.RENDER_COMPONENT.get(entity);

        TextureRegion region = renderComponent.getRegion();

        if (bodyComponent != null) {

            Body body = bodyComponent.getBody();

            spriteBatch.draw(region,
                    body.getPosition().x - renderComponent.getWidth() / 2, body.getPosition().y - renderComponent.getHeight() / 2,
                    renderComponent.getWidth() / 2, renderComponent.getHeight() /2,
                    renderComponent.getWidth(), renderComponent.getHeight(),
                    1.0f, 1.0f,
                    (MathUtils.radDeg * body.getAngle())
            );

        } else { // for background rendering

            spriteBatch.draw(region,
                    Constants.WORLD_WIDTH / 2 - renderComponent.getWidth() / 2, Constants.WORLD_HEIGHT / 2 - renderComponent.getHeight() / 2,
                    renderComponent.getWidth(), renderComponent.getHeight()
            );

        }

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
