package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.IMapElement;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GuiElementBox {
    private ArrayList<Image> sourceImages = new ArrayList<>();
    private ArrayList<ImageView> sprites = new ArrayList<>();
    private Label label;
    public GuiElementBox(IMapElement el, double w, double h, ImageLoader imageLoader) throws FileNotFoundException {
        sourceImages.add( imageLoader.getImage(el.getFilename()));
        if(el.getClass() == Animal.class)
            sourceImages.add(imageLoader.getImage(((Animal) el).getEnergyFilename()));

        for (Image src: sourceImages ) {
            ImageView tmpSprite = new ImageView(src);
            tmpSprite.setFitHeight(h);
            tmpSprite.setFitWidth(w);
            sprites.add(tmpSprite);
        }
    }

    public VBox createBox(){
        VBox tmp = new VBox();
        tmp.getChildren().addAll(sprites);
        tmp.setAlignment( Pos.CENTER);
        return tmp;
    }
}
