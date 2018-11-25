package com.alefa.around.utils;

import com.alefa.around.manager.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/** A simple utility class for GUIs.
 * It contains helper methods for creating Scene2D GUIs from the String references in Assets.
 * @author LEEFAMILY */

public class GUIBuilder {

    public static Image createImage(Assets assets, String name) {
        Image image = new Image(assets.getRegion(name));
        image.setScaling(Scaling.fit);
        return image;
    }

    public static Label createLabel(Assets assets, String text, String font, Color color) {
        return new Label(text, new Label.LabelStyle(assets.getFont(font), color));
    }

    public static Label createLabel(Assets assets, String text, String font) {
        return new Label(text, new Label.LabelStyle(assets.getFont(font), Constants.UI_FONT_COLOR));
    }

    public static Button createImageButton(Assets assets, String imageUp, String imageDown) {
        return new Button(new TextureRegionDrawable(assets.getRegion(imageUp)), new TextureRegionDrawable(assets.getRegion(imageDown)));
    }

    public static void changeImageButton(Assets assets, Button button, String newImageUp, String newImageDown) {
        button.getStyle().up = new TextureRegionDrawable(assets.getRegion(newImageUp));
        button.getStyle().down = new TextureRegionDrawable(assets.getRegion(newImageDown));
    }

}
