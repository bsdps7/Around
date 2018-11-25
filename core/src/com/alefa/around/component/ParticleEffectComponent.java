package com.alefa.around.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Pool;

public class ParticleEffectComponent implements Component, Pool.Poolable {

    private ParticleEffectPool.PooledEffect pooledEffect;

    public ParticleEffectPool.PooledEffect getPooledEffect() {
        return pooledEffect;
    }

    public void setPooledEffect(ParticleEffectPool.PooledEffect pooledEffect) {
        this.pooledEffect = pooledEffect;
    }

    @Override
    public void reset() {
        pooledEffect.free();
        pooledEffect = null;
    }
}
