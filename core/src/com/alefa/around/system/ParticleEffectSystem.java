package com.alefa.around.system;

import com.alefa.around.component.ParticleEffectComponent;
import com.alefa.around.utils.ComponentMappers;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;


public class ParticleEffectSystem extends IteratingSystem {

    private static final String TAG = ParticleEffectSystem.class.getSimpleName();

    /* -- Constants -- */
    private static final Family FAMILY = Family.all(
            ParticleEffectComponent.class
    ).get();

    /* -- Fields -- */
    private Array<Entity> renderQueue;
    private SpriteBatch spriteBatch;
    private Viewport viewport;

    /* -- Constructor -- */
    public ParticleEffectSystem(Viewport viewport, SpriteBatch spriteBatch) {
        super(FAMILY);

        renderQueue = new Array<Entity>();
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;
    }

    /* -- Public methods -- */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        for (int i = 0, n = renderQueue.size; i < n; i++) {

            ParticleEffectComponent particleEffectComponent = ComponentMappers.PARTICLE_EFFECT_COMPONENT.get(renderQueue.get(i));
            if (i == 0) {
                particleEffectComponent.getPooledEffect().setEmittersCleanUpBlendFunction(false); // if set to true, effect will return blend function back to normal afte drawing
            }
            particleEffectComponent.getPooledEffect().draw(spriteBatch, deltaTime);
            Gdx.app.log(TAG, "update: Draw particle");
        }

        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // required to return the blend function back to normal

        spriteBatch.end();

        renderQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        ParticleEffectComponent particleEffectComponent = ComponentMappers.PARTICLE_EFFECT_COMPONENT.get(entity);

        if (particleEffectComponent.getPooledEffect().isComplete()) {

            getEngine().removeEntity(entity);
            Gdx.app.log(TAG, "processEntity: Removed particle");

        } else {

            renderQueue.add(entity);

        }

    }
}
