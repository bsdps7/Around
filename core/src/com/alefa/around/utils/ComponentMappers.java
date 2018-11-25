package com.alefa.around.utils;


import com.alefa.around.component.BodyComponent;
import com.alefa.around.component.CollisionComponent;
import com.alefa.around.component.ObstacleComponent;
import com.alefa.around.component.ParticleEffectComponent;
import com.alefa.around.component.PlayerComponent;
import com.alefa.around.component.RenderComponent;
import com.alefa.around.component.StateComponent;
import com.alefa.around.component.TypeComponent;
import com.badlogic.ashley.core.ComponentMapper;


/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class ComponentMappers {

    /* -- Constants -- */
    public static final ComponentMapper<BodyComponent> BODY_COMPONENT = ComponentMapper.getFor(BodyComponent.class);
    public static final ComponentMapper<CollisionComponent> COLLISION_COMPONENT = ComponentMapper.getFor(CollisionComponent.class);
    public static final ComponentMapper<ObstacleComponent> OBSTACLE_COMPONENT = ComponentMapper.getFor(ObstacleComponent.class);
    public static final ComponentMapper<ParticleEffectComponent> PARTICLE_EFFECT_COMPONENT = ComponentMapper.getFor(ParticleEffectComponent.class);
    public static final ComponentMapper<PlayerComponent> PLAYER_COMPONENT = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<RenderComponent> RENDER_COMPONENT = ComponentMapper.getFor(RenderComponent.class);
    public static final ComponentMapper<StateComponent> STATE_COMPONENT = ComponentMapper.getFor(StateComponent.class);
    public static final ComponentMapper<TypeComponent> TYPE_COMPONENT = ComponentMapper.getFor(TypeComponent.class);

}
