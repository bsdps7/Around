package com.alefa.around.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Logger;

/** A simple class to manage assets. Holds all references and paths to assets. Needs to be disposed.
 * @author LEEFAMILY */

public class Assets implements Disposable {

    private static final String TAG = Assets.class.getSimpleName();

    public static final String BACKGROUND_PATH = "textures/background.png"; // TODO: 11/25/2018 Replace background texture with shaders

    public static final class Regions {

        /** Gameplay regions */
        public static final String FLOOR = "floor";
        public static final String FLOOR_SHADOW = "shadow_floor";

        public static final String[]
                PLAYER = {
                "player_00", "player_01", "player_02", "player_03", "player_04", "player_05", "player_06", "player_07", "player_08", "player_09", "player_10", "player_11"
        },
                PLAYER_SHADOW = {
                "player_00_shadow", "player_01_shadow", "player_02_shadow", "player_03_shadow", "player_04_shadow", "player_05_shadow", "player_06_shadow", "player_07_shadow", "player_08_shadow", "player_09_shadow", "player_10_shadow", "player_11_shadow"
        };

        public static final String[]
                OBSTACLE = {
                "obstacle_02", "obstacle_03", "obstacle_04", "obstacle_06", "obstacle_08", "obstacle_09", "obstacle_10"
        },
                OBSTACLE_SHADOW = {
                "obstacle_02_shadow", "obstacle_03_shadow", "obstacle_04_shadow", "obstacle_06_shadow", "obstacle_08_shadow", "obstacle_09_shadow", "obstacle_10_shadow"
        };

        /** GUI regions */
        public static final String GUI_BUTTON_CANCEL = "gui_btn_cancel", GUI_BUTTON_CANCEL_SHADOW = "gui_btn_cancel_shadow";
        public static final String GUI_BUTTON_GIFT = "gui_btn_gift", GUI_BUTTON_GIFT_SHADOW = "gui_btn_gift_shadow";
        public static final String GUI_BUTTON_SHOP = "gui_btn_shop", GUI_BUTTON_SHOP_SHADOW = "gui_btn_shop_shadow";
        public static final String GUI_BUTTON_HELP = "gui_btn_help", GUI_BUTTON_HELP_SHADOW = "gui_btn_help_shadow";
        public static final String GUI_BUTTON_PAUSE = "gui_btn_pause", GUI_BUTTON_PAUSE_SHADOW = "gui_btn_pause_shadow";
        public static final String GUI_BUTTON_RESUME = "gui_btn_resume", GUI_BUTTON_RESUME_SHADOW = "gui_btn_resume_shadow";
        public static final String GUI_BUTTON_SOUND_OFF = "gui_btn_soundoff", GUI_BUTTON_SOUND_OFF_SHADOW = "gui_btn_soundoff_shadow";
        public static final String GUI_BUTTON_SOUND_ON = "gui_btn_soundon", GUI_BUTTON_SOUND_ON_SHADOW = "gui_btn_soundon_shadow";

        public static final String GUI_GEM = "gui_gem", GUI_GEM_SHADOW = "gui_gem_shadow";
        public static final String GUI_SHOP_GEM = "gui_shop_gem", GUI_SHOP_GEM_SHADOW = "gui_shop_gem_shadow";
        public static final String GUI_SHOP_SELECTED = "gui_shop_selected", GUI_SHOP_SELECTED_SHADOW = "gui_shop_selected_shadow";
        public static final String GUI_SHOP_UNSELECTED = "gui_shop_unselected", GUI_SHOP_UNSELECTED_SHADOW = "gui_shop_unselected_shadow";

        public static final String POPUP_HELP_LEFT_FINGER = "popup_help_left_finger", POPUP_HELP_RIGHT_FINGER = "popup_help_right_finger";
        public static final String POPUP_HELP_TAP = "popup_help_tap";

        public static final String TITLE_MAIN = "title_main", TITLE_MAIN_SHADOW = "title_main_shadow";
        public static final String TITLE_SHOP = "title_shop", TITLE_SHOP_SHADOW = "title_shop_shadow";

        /** Splash screen regions */
        public static final String SPLASH_LOGO = "splash_logo";

    }

    public static final class Fonts {

        public static final String
                SIZE36 = "mostra36.otf",
                SIZE56 = "mostra56.otf",
                SIZE84 = "mostra84.otf",
                SIZE120 = "mostra120.otf",
                SIZE200 = "mostra210.otf";

    }

    public static final class Audio {

        public static final String BG_MUSIC_1 = MUSIC_PHI_PATH;
        public static final String BG_MUSIC_2 = MUSIC_NFN_PATH;

        public static final String SHATTER_SOUND = SOUND_SHATTER_PATH;
        public static final String GEM_SOUND = SOUND_GEM_PATH;

    }

    public static final class Pfx {

        public static final String SHATTER = PFX_SHATTER_PATH;

    }

    /** Paths for the assets */
    private static final String ATLAS_GAMEPLAY_PATH = "textures/gameplay.atlas";
    private static final String ATLAS_GUI_PATH = "textures/gui.atlas";
    private static final String ATLAS_OBSTACLES_PATH = "textures/obstacles.atlas";
    private static final String ATLAS_PLAYERS_PATH = "textures/players.atlas";
    private static final String ATLAS_SPLASH_PATH = "textures/splash.atlas";

    private static final String FONT_MOSTRA_PATH = "fonts/mostra.otf";

    private static final String MUSIC_PHI_PATH = "audio/phi.ogg";
    private static final String MUSIC_NFN_PATH = "audio/notfornothing.ogg";
    private static final String SOUND_SHATTER_PATH = "audio/shatter.ogg";
    private static final String SOUND_GEM_PATH = "audio/gem.ogg";

    private static final String PFX_SHATTER_PATH = "pfx/shatter.p";

    private final AssetManager assetManager;

    public Assets() {
        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);
    }

    /** Loads the assets for the loading splash screen. */
    public void loadLoading() {
        assetManager.load(ATLAS_SPLASH_PATH, TextureAtlas.class);
        assetManager.finishLoading();   // stops thread until all assets on queue are loaded, synchronous
    }

    /** Loads all assets asynchronously. */
    public void loadAll() {
        loadFonts();
        loadTextures();
        loadPfx();
        loadAudios();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    private void loadTextures() {
        assetManager.load(BACKGROUND_PATH, Texture.class);

        assetManager.load(ATLAS_GAMEPLAY_PATH, TextureAtlas.class);
        assetManager.load(ATLAS_GUI_PATH, TextureAtlas.class);
        assetManager.load(ATLAS_OBSTACLES_PATH, TextureAtlas.class);
        assetManager.load(ATLAS_PLAYERS_PATH, TextureAtlas.class);
    }

    private void loadFonts() {
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        assetManager.setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(new InternalFileHandleResolver()));

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameterPoiret36 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameterPoiret36.fontFileName = FONT_MOSTRA_PATH;
        parameterPoiret36.fontParameters.size = 36;
        parameterPoiret36.fontParameters.magFilter = Texture.TextureFilter.Linear;
        parameterPoiret36.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(Fonts.SIZE36, BitmapFont.class, parameterPoiret36);

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameterPoiret56 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameterPoiret56.fontFileName = FONT_MOSTRA_PATH;
        parameterPoiret56.fontParameters.size = 56;
        parameterPoiret56.fontParameters.magFilter = Texture.TextureFilter.Linear;
        parameterPoiret56.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(Fonts.SIZE56, BitmapFont.class, parameterPoiret56);

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameterPoiret84 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameterPoiret84.fontFileName = FONT_MOSTRA_PATH;
        parameterPoiret84.fontParameters.size = 84;
        parameterPoiret84.fontParameters.magFilter = Texture.TextureFilter.Linear;
        parameterPoiret84.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(Fonts.SIZE84, BitmapFont.class, parameterPoiret84);

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameterPoiret120 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameterPoiret120.fontFileName = FONT_MOSTRA_PATH;
        parameterPoiret120.fontParameters.size = 120;
        parameterPoiret120.fontParameters.magFilter = Texture.TextureFilter.Linear;
        parameterPoiret120.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(Fonts.SIZE120, BitmapFont.class, parameterPoiret120);

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameterPoiret200 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameterPoiret200.fontFileName = FONT_MOSTRA_PATH;
        parameterPoiret200.fontParameters.size = 200;
        parameterPoiret200.fontParameters.magFilter = Texture.TextureFilter.Linear;
        parameterPoiret200.fontParameters.minFilter = Texture.TextureFilter.Linear;
        assetManager.load(Fonts.SIZE200, BitmapFont.class, parameterPoiret200);
    }

    private void loadAudios() {
        assetManager.load(MUSIC_PHI_PATH, Music.class);
        assetManager.load(MUSIC_NFN_PATH, Music.class);


        assetManager.load(SOUND_SHATTER_PATH, Sound.class);
        assetManager.load(SOUND_GEM_PATH, Sound.class);
    }

    private void loadPfx() {
        ParticleEffectLoader.ParticleEffectParameter particleEffectParameter = new ParticleEffectLoader.ParticleEffectParameter();
        particleEffectParameter.atlasFile = ATLAS_GAMEPLAY_PATH;

        assetManager.load(PFX_SHATTER_PATH, ParticleEffect.class, particleEffectParameter);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureRegion getRegion(String name) {
        if (name.equals(BACKGROUND_PATH)) {
            return new TextureRegion(assetManager.get(BACKGROUND_PATH, Texture.class));
        }

        TextureAtlas[] textureAtlases = (assetManager.getAll(TextureAtlas.class, new Array<TextureAtlas>())).toArray(TextureAtlas.class);

        for (TextureAtlas textureAtlas : textureAtlases) {
            TextureRegion region = textureAtlas.findRegion(name);
            if (region != null) return region;
        }

        // if region with the given name is not found log and return null

        Gdx.app.error(TAG, "getRegion: Region with given name not found", null);
        return null;
    }

    public BitmapFont getFont(String name) {
        return assetManager.get(name, BitmapFont.class);
    }

    public Sound getSound(String name) {
        return assetManager.get(name, Sound.class);
    }

    public Music getMusic(String name) {
        return assetManager.get(name, Music.class);
    }

    public ParticleEffect getParticleEffect(String name) {
        return assetManager.get(name, ParticleEffect.class);
    }

}
