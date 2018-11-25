package com.alefa.around.manager;

import com.alefa.around.utils.EntityFactory;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.IntMap;

/**
 * A class which holds the pools for particles
 *
 * Created by LEEFAMILY on 2018-05-22.
 */

public class PfxManager {

    /* -- Fields -- */
    private IntMap<ParticleEffectPool> particleEffectPools;

    private Assets assets;

    /* -- Constructor -- */
    public PfxManager(Assets assets) {
        particleEffectPools = new IntMap<ParticleEffectPool>();

        this.assets = assets;
        initParticleEffects();

    }

    /* -- Public methods -- */
    public ParticleEffectPool.PooledEffect getPooledParticleEffect(int type) { // obtains a pooled particle effect of type from pool
        return particleEffectPools.get(type).obtain();
    }

    /* -- Private methods -- */
    private void initParticleEffects() {

        ParticleEffect shatterEffect = assets.getParticleEffect(Assets.Pfx.SHATTER);
        particleEffectPools.put(EntityFactory.Pfx.SHATTER, new ParticleEffectPool(shatterEffect, 5, 20));

    }

}
