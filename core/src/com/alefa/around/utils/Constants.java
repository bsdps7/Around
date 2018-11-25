package com.alefa.around.utils;

import com.badlogic.gdx.graphics.Color;

public class Constants {

    // Game
    public static final float WORLD_WIDTH = 9f, WORLD_HEIGHT = 16f;
    public static final float WORLD_EXTENDED_WIDTH = 12f, WORLD_EXTENDED_HEIGHT = 19.5f;

    public static final float GAMEOVER_DELAY = 0.5f;

    // UI
    public static final float UI_UNIT = 80f;

    public static final float HUD_WIDTH = 9 * UI_UNIT, HUD_HEIGHT = 16 * UI_UNIT;

    public static final Color UI_FONT_COLOR = new Color(1f, 1f, 1f, 1f);

    /* -- Entities -- */

    public static final float FLOOR_RADIUS = 1.75f;

    public static final float PLAYER_RADIUS_X = 0.3f, PLAYER_RADIUS_Y = 0.375f;
    public static final float PLAYER_PATH_RADIUS = FLOOR_RADIUS + 0.5f;
    public static final float PLAYER_INIT_ANGLE_DEG = 90f; // player's initial angle position in degrees
    public static final float PLAYER_ANGLE_SPEED_DEG = 480f; // per second

    public static final float OBSTACLE_INIT_RADIUS = WORLD_WIDTH;
    public static final float OBSTACLE_SPAWN_INTERVAL = 0.8f; // in seconds
    public static final float OBSTACLE_SPEED = -4.5f; // per second

    /* -- Box2D physics -- */

    public static final int CIRCLE_VERTICES_NUM = 18; // number of vertices generated circles will have

    public static final float BOX2D_FPS = 60.0f;
    public static final int BOX2D_VELOCITY_ITERATIONS = 6, BOX2D_POSITION_ITERATIONS = 2;

    /* -- Collision filters -- */

    public static final class CATEGORY_BITS {

        public static final short FLOOR = 0x001;
        public static final short PLAYER = 0x002;
        public static final short OBSTACLE = 0x004;

    }

    public static final class MASK_BITS {

        public static final short FLOOR = CATEGORY_BITS.OBSTACLE;
        public static final short PLAYER = CATEGORY_BITS.OBSTACLE;
        public static final short OBSTACLE = CATEGORY_BITS.PLAYER | CATEGORY_BITS.FLOOR;

    }

}
