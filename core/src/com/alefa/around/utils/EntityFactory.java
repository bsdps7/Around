package com.alefa.around.utils;

import com.alefa.around.component.BodyComponent;
import com.alefa.around.component.CollisionComponent;
import com.alefa.around.component.ObstacleComponent;
import com.alefa.around.component.ParticleEffectComponent;
import com.alefa.around.component.PlayerComponent;
import com.alefa.around.component.RenderComponent;
import com.alefa.around.component.StateComponent;
import com.alefa.around.component.TypeComponent;
import com.alefa.around.manager.Assets;
import com.alefa.around.manager.PfxManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by LEEFAMILY on 2018-03-24.
 */

public class EntityFactory {

    private static final String TAG = EntityFactory.class.getSimpleName();

    /* -- Constants -- */
    public static final class Background {

    }

    public static final class Floor {

    }

    public static final class Player {

    }

    public static final class Obstacle {

    }

    public static final class Pfx {

        public static final int SHATTER = 0;

    }

    /* -- Fields -- */
    private World world;
    private PooledEngine engine;

    private Assets assets;
    private PfxManager pfxManager;

    /* -- Constructor -- */
    public EntityFactory(World world, PooledEngine engine, Assets assets, PfxManager pfxManager) {
        this.world = world;
        this.engine = engine;
        this.assets = assets;
        this.pfxManager = pfxManager;

        // unsatisfactory solution to strange bug where first particles do not draw properly; immediately creates the first particle, can be anywhere off screen
        createParticleEffect(Pfx.SHATTER, -10000, -10000);

    }

    /* -- Public methods -- */
    public void createBackground() {
        Entity bgEntity = engine.createEntity();

        RenderComponent bgRenderComponent = engine.createComponent(RenderComponent.class);
        bgRenderComponent.setHeight(Constants.WORLD_EXTENDED_HEIGHT);
        bgRenderComponent.setWidth(Constants.WORLD_EXTENDED_WIDTH);
        bgRenderComponent.setZ(0);
        bgRenderComponent.setRegion(assets.getRegion(Assets.BACKGROUND_PATH));

        bgEntity.add(bgRenderComponent);

        engine.addEntity(bgEntity);

    }

    public void createFloor() {

        Entity entity = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2);
        Body body = world.createBody(bodyDef);
        body.setUserData(entity);

        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.FLOOR_RADIUS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BITS.FLOOR;
        fixtureDef.filter.maskBits = Constants.MASK_BITS.FLOOR;
        body.createFixture(fixtureDef);

        bodyComponent.setBody(body);

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);

        RenderComponent renderComponent = engine.createComponent(RenderComponent.class);
        renderComponent.setHeight(Constants.FLOOR_RADIUS * 2);
        renderComponent.setWidth(Constants.FLOOR_RADIUS * 2);
        renderComponent.setZ(1);
        renderComponent.setRegion(assets.getRegion(Assets.Regions.FLOOR));

        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        typeComponent.setType(TypeComponent.TYPE_FLOOR);

        entity.add(bodyComponent);
        entity.add(collisionComponent);
        entity.add(renderComponent);
        entity.add(typeComponent);

        engine.addEntity(entity);
    }

    public void createPlayer() {

        Entity entity = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2 + Constants.PLAYER_PATH_RADIUS);
        Body body = world.createBody(bodyDef);
        body.setUserData(entity);

        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = {new Vector2(0, Constants.PLAYER_RADIUS_Y), new Vector2(-Constants.PLAYER_RADIUS_X, -Constants.PLAYER_RADIUS_Y), new Vector2(Constants.PLAYER_RADIUS_X, -Constants.PLAYER_RADIUS_Y)};
        shape.set(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BITS.PLAYER;
        fixtureDef.filter.maskBits = Constants.MASK_BITS.PLAYER;
        body.createFixture(fixtureDef);

        bodyComponent.setBody(body);

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        playerComponent.setAnglePosDeg(Constants.PLAYER_INIT_ANGLE_DEG);

        RenderComponent renderComponent = engine.createComponent(RenderComponent.class);
        renderComponent.setHeight(Constants.PLAYER_RADIUS_Y * 2);
        renderComponent.setWidth(Constants.PLAYER_RADIUS_X * 2);
        renderComponent.setZ(2);
        renderComponent.setRegion(assets.getRegion(Assets.Regions.PLAYER[0]));

        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        stateComponent.setState(StateComponent.STATE_NORMAL);

        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        typeComponent.setType(TypeComponent.TYPE_PLAYER);

        entity.add(bodyComponent);
        entity.add(collisionComponent);
        entity.add(playerComponent);
        entity.add(renderComponent);
        entity.add(stateComponent);
        entity.add(typeComponent);

        engine.addEntity(entity);
    }

    public void createObstacle(int type, float angularSpeedDeg, float initAngleDeg) {

        Entity entity = engine.createEntity();

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2);
        bodyDef.angle = MathUtils.degRad * initAngleDeg;
        bodyDef.angularVelocity = MathUtils.degRad * angularSpeedDeg;
        Body body = world.createBody(bodyDef);
        body.setUserData(entity);

        ChainShape shape = new ChainShape();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.CATEGORY_BITS.OBSTACLE;
        fixtureDef.filter.maskBits = Constants.MASK_BITS.OBSTACLE;
        body.createFixture(fixtureDef);

        bodyComponent.setBody(body);

        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);

        ObstacleComponent obstacleComponent = engine.createComponent(ObstacleComponent.class);
        switch (type) {
            case 0:
                obstacleComponent.setNumSections(12f / (12f - 2f));
                break;
            case 1:
                obstacleComponent.setNumSections(12f / (12f - 3f));
                break;
            case 2:
                obstacleComponent.setNumSections(12f / (12f - 4f));
                break;
            case 3:
                obstacleComponent.setNumSections(12f / (12f - 6f));
                break;
            case 4:
                obstacleComponent.setNumSections(12f / (12 - 8f));
                break;
            case 5:
                obstacleComponent.setNumSections(12f / (12f - 9f));
                break;
            default:
                obstacleComponent.setNumSections(12f / (12f - 6f));
                break;
        }
        obstacleComponent.setAngularSpeed(angularSpeedDeg);

        RenderComponent renderComponent = engine.createComponent(RenderComponent.class);
        renderComponent.setHeight(Constants.OBSTACLE_INIT_RADIUS * 2);
        renderComponent.setWidth(Constants.OBSTACLE_INIT_RADIUS * 2);
        renderComponent.setZ(3);
        renderComponent.setRegion(assets.getRegion(Assets.Regions.OBSTACLE[type]));

        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        stateComponent.setState(StateComponent.STATE_NORMAL);

        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        typeComponent.setType(TypeComponent.TYPE_OBSTACLE);

        entity.add(bodyComponent);
        entity.add(collisionComponent);
        entity.add(obstacleComponent);
        entity.add(renderComponent);
        entity.add(stateComponent);
        entity.add(typeComponent);

        engine.addEntity(entity);
    }

    public void createParticleEffect(int type, float x, float y) {

        Gdx.app.log(TAG, "createParticleEffect" + type);

        Entity entity = engine.createEntity();

        ParticleEffectComponent particleEffectComponent = engine.createComponent(ParticleEffectComponent.class);
        particleEffectComponent.setPooledEffect(pfxManager.getPooledParticleEffect(type));
        particleEffectComponent.getPooledEffect().setPosition(x, y);

        entity.add(particleEffectComponent);

        engine.addEntity(entity);
    }

}
