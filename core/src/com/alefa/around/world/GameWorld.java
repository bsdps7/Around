package com.alefa.around.world;

import com.alefa.around.AroundGame;
import com.alefa.around.event.EventQueue;
import com.alefa.around.event.GameEvent;
import com.alefa.around.manager.Assets;
import com.alefa.around.manager.PfxManager;
import com.alefa.around.system.CollisionSystem;
import com.alefa.around.system.DebugRenderSystem;
import com.alefa.around.system.ObstacleSpawnSystem;
import com.alefa.around.system.ObstacleSystem;
import com.alefa.around.system.ParticleEffectSystem;
import com.alefa.around.system.PlayerSystem;
import com.alefa.around.system.RenderSystem;
import com.alefa.around.system.WorldSystem;
import com.alefa.around.utils.Constants;
import com.alefa.around.utils.EntityFactory;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Handles and controls the game events
 * and is responsible for rendering and updating the game logic
 * of the game but maintains no UI of its own
 */

public class GameWorld extends InputAdapter implements Disposable {

    private static final String TAG = GameWorld.class.getSimpleName();

    /* -- Fields -- */
    private AroundGame game;

    private PooledEngine engine;
    private World world;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;

    private PfxManager pfxManager;
    private EntityFactory entityFactory;
    private EventQueue eventQueue;

    private boolean leftTouched = false, rightTouched = false;
    private boolean logicPaused = false;
    private boolean playerDead = false;

    private float gameoverTimer = Constants.GAMEOVER_DELAY;

    private int tempScore = 0, tempGems = 0;

    /* -- Constructor -- */
    public GameWorld(AroundGame game) {
        this.game = game;

        engine = new PooledEngine();
        world = new World(new Vector2(0, 0), true);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, Constants.WORLD_EXTENDED_WIDTH, Constants.WORLD_EXTENDED_HEIGHT, camera);
        spriteBatch = new SpriteBatch();

        pfxManager = new PfxManager(game.getAssets());
        entityFactory = new EntityFactory(world, engine, game.getAssets(), pfxManager);
        eventQueue = new EventQueue();
        game.getGameEventSignal().add(eventQueue);

        addSystems();
        init();
    }

    /* -- Public methods -- */
    public void update(float delta) {
        engine.update(delta);

        if (!logicPaused) {
            // listen events when logic is not paused
            for (GameEvent gameEvent : eventQueue.getEvents()) {

                switch (gameEvent) {

                    case SCORE:
                        tempScore += 1;
                        if (tempScore % 10 == 0) { // if score is a multiple of ten, get a gem
                            game.getGameEventSignal().dispatch(GameEvent.GEM);
                        }

                        break;

                    case GEM:
                        tempGems += tempScore / 50 + 1; // the number of gems you recieve increase every 50 points
                        game.getPrefsManager().setGems(game.getPrefsManager().getGems() + tempScore / 50 + 1); // the number of gems you recieve increase every 50 points

                        game.getAudioManager().playGameplaySound(Assets.Audio.GEM_SOUND);

                        break;

                    case PLAYER_DEAD:
                        playerDead = true;

                        game.getAudioManager().playGameplaySound(Assets.Audio.SHATTER_SOUND);
                        break;

                    default:
                        break;

                }

            }
        }

        if (playerDead) {
            gameoverTimer -= delta;

            if (gameoverTimer <= 0) {
                game.getGameEventSignal().dispatch(GameEvent.GAMEOVER);
                playerDead = false;
            }
        }

    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);

        // moves the camera so the world is in the center, as the ExtendViewport only extends in one direction
        camera.translate(-(viewport.getWorldWidth() - Constants.WORLD_WIDTH) / 2, -(viewport.getWorldHeight() - Constants.WORLD_HEIGHT) / 2);
    }

    public void pauseLogic() { // the GameWorld continues rendering, but the logic does not update
        if (logicPaused) return;
        logicPaused = true;

        engine.getSystem(WorldSystem.class).setProcessing(false);
        engine.getSystem(ObstacleSpawnSystem.class).setProcessing(false);
        engine.getSystem(ObstacleSystem.class).setProcessing(false);
        engine.getSystem(CollisionSystem.class).setProcessing(false);
    }

    public void resumeLogic() {
        if (!logicPaused) return;
        logicPaused = false;

        engine.getSystem(WorldSystem.class).setProcessing(true);
        engine.getSystem(ObstacleSpawnSystem.class).setProcessing(true);
        engine.getSystem(ObstacleSystem.class).setProcessing(true);
        engine.getSystem(CollisionSystem.class).setProcessing(true);
    }

    @Override
    public void dispose() {

        for (EntitySystem system : engine.getSystems()) {
            if (system instanceof Disposable)
                ((Disposable) system).dispose();
        }

        engine.removeAllEntities();
        engine.clearPools();
        world.dispose();

    }

    public void reset() {

        playerDead = false;
        gameoverTimer = Constants.GAMEOVER_DELAY;

        tempScore = 0;
        tempGems = 0;

        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            world.destroyBody(body);
        }

        engine.removeAllEntities();
        engine.clearPools();

        leftTouched = false;
        rightTouched = false;

        init();

    }

    /* -- Private methods -- */
    private void init() {

        entityFactory.createBackground();
        entityFactory.createFloor();
        entityFactory.createPlayer();
    }

    private void addSystems() {
        engine.addSystem(new WorldSystem(world));
        engine.addSystem(new PlayerSystem(this));
        engine.addSystem(new ObstacleSpawnSystem(entityFactory));
        engine.addSystem(new ObstacleSystem());
        engine.addSystem(new CollisionSystem(world, game.getGameEventSignal(), entityFactory));
        engine.addSystem(new RenderSystem(viewport, spriteBatch));
        engine.addSystem(new ParticleEffectSystem(viewport, spriteBatch));
//        engine.addSystem(new DebugRenderSystem(world, camera));
    }

    /* -- Input methods -- */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (pointer > 0) // ignore multi-touches (touches are ordered in the order the fingers touched)
            return true;

        Vector2 pos = new Vector2(screenX, screenY);
        pos = viewport.unproject(pos);
        Gdx.app.log(TAG, "touchDown: " + pos.x);

        if (pos.x < Constants.WORLD_WIDTH / 2) {
            leftTouched = true;
        } else {
            rightTouched = true;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (pointer > 0) // ignore multi-touches
            return true;

        Vector2 pos = new Vector2(screenX, screenY);
        pos = viewport.unproject(pos);
        Gdx.app.log(TAG, "touchUp: " + pos.x);

        if (pos.x < Constants.WORLD_WIDTH / 2) {
            leftTouched = false;
        } else {
            rightTouched = false;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (pointer > 0) // ignore multi-touches
            return true;

        Vector2 pos = new Vector2(screenX, screenY);
        pos = viewport.unproject(pos);
        Gdx.app.log(TAG, "touchDragged: " + pos.x);

        if (pos.x < Constants.WORLD_WIDTH / 2) {
            if (rightTouched) { // if touch dragged to leftTouched and rightTouched is true
                rightTouched = false;
                leftTouched = true;
            }
        } else {
            if (leftTouched) { // if touch dragged to rightTouched and leftTouched is true
                leftTouched = false;
                rightTouched = true;
            }
        }

        return true;
    }

    /* -- Getters -- */
    public boolean isLeftTouched() {
        return leftTouched;
    }

    public boolean isRightTouched() {
        return rightTouched;
    }

    public int getTempScore() {
        return tempScore;
    }

    public int getTempGems() {
        return tempGems;
    }

}
