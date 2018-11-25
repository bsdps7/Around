package com.alefa.around.system;

import com.alefa.around.component.CollisionComponent;
import com.alefa.around.component.StateComponent;
import com.alefa.around.component.TypeComponent;
import com.alefa.around.event.GameEvent;
import com.alefa.around.utils.ComponentMappers;
import com.alefa.around.utils.EntityFactory;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by LEEFAMILY on 2018-03-25.
 */

public class CollisionSystem extends IteratingSystem implements ContactListener {

    private static final String TAG = CollisionSystem.class.getSimpleName();

    /* -- Constants -- */
    private static final Family FAMILY = Family.all(
            CollisionComponent.class,
            TypeComponent.class
    ).get();

    /* -- Fields -- */
    private final Signal<GameEvent> gameEventSignal;
    private EntityFactory entityFactory;

    /* -- Constructor -- */
    public CollisionSystem(World world, Signal<GameEvent> gameEventSignal, EntityFactory entityFactory) {
        super(FAMILY);

        world.setContactListener(this);
        this.gameEventSignal = gameEventSignal;
        this.entityFactory = entityFactory;
    }

    /* -- Public methods -- */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        CollisionComponent collisionComponent = ComponentMappers.COLLISION_COMPONENT.get(entity);
        Entity collidedEntity = collisionComponent.getCollidedEntity();

        if (collidedEntity != null) {

            TypeComponent typeComponent = ComponentMappers.TYPE_COMPONENT.get(collidedEntity);
            if (typeComponent != null) {
                int type = typeComponent.getType();

                StateComponent stateComponent = ComponentMappers.STATE_COMPONENT.get(entity);

                switch (type) {

                    case TypeComponent.TYPE_FLOOR:

                        if (stateComponent != null) {

                            // obstacle hit floor
                            if (ComponentMappers.TYPE_COMPONENT.get(entity).getType() == TypeComponent.TYPE_OBSTACLE) {
                                stateComponent.setState(StateComponent.STATE_DEAD);

                                gameEventSignal.dispatch(GameEvent.SCORE);
                            }
                        }

                        break;

                    case TypeComponent.TYPE_PLAYER: // hit player

                        if (stateComponent != null) {

                            // obstacle hit player
                            if (ComponentMappers.TYPE_COMPONENT.get(entity).getType() == TypeComponent.TYPE_OBSTACLE) {
                                stateComponent.setState(StateComponent.STATE_DEAD);

                                Gdx.app.log(TAG, "processEntity: Obstacle hit player");
                            }
                        }

                        break;

                    case TypeComponent.TYPE_OBSTACLE: // hit obstacle

                        if (stateComponent != null) {

                            // player hit obstacle
                            if (ComponentMappers.TYPE_COMPONENT.get(entity).getType() == TypeComponent.TYPE_PLAYER) {
                                stateComponent.setState(StateComponent.STATE_DEAD);

                                Body playerBody = ComponentMappers.BODY_COMPONENT.get(entity).getBody();
                                if (playerBody != null) {

                                    entityFactory.createParticleEffect(EntityFactory.Pfx.SHATTER, playerBody.getPosition().x, playerBody.getPosition().y);

                                }

                                Gdx.app.log(TAG, "processEntity: Player hit obstacle");

                                gameEventSignal.dispatch(GameEvent.PLAYER_DEAD);
                            }

                        }

                        break;

                    default:
                        break;

                }
            }

        }

    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getBody().getUserData() instanceof Entity && fixtureB.getBody().getUserData() instanceof Entity) {

            Entity entityA = (Entity) fixtureA.getBody().getUserData();
            Entity entityB = (Entity) fixtureB.getBody().getUserData();

            CollisionComponent collisionComponentA = ComponentMappers.COLLISION_COMPONENT.get(entityA);
            CollisionComponent collisionComponentB = ComponentMappers.COLLISION_COMPONENT.get(entityB);

            if (collisionComponentA != null && collisionComponentB != null) {

                collisionComponentA.setCollidedEntity((Entity) fixtureB.getBody().getUserData());
                collisionComponentB.setCollidedEntity((Entity) fixtureA.getBody().getUserData());

            }

        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
