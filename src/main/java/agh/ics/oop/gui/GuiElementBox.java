package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Image sourceImage;
    private ImageView sprite;
    private Label label;
    public GuiElementBox(IMapElement el, double w, double h, ImageLoader imageLoader) throws FileNotFoundException {
        sourceImage = imageLoader.getImage(el.getFilename());
        sprite = new ImageView(sourceImage);
        sprite.setFitHeight(h);
        sprite.setFitWidth(w);

        label =new Label(el.toString());
    }

    public VBox createBox(){
        VBox tmp = new VBox(sprite,label);
        tmp.setAlignment( Pos.CENTER);
        return tmp;
    }
}
