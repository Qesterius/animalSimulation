package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Vector2d;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageLoader {
    protected Map<String, Image> imageMap = new LinkedHashMap<>();

    Image getImage(String src) throws FileNotFoundException {
        Image get = imageMap.get(src);
        if( get == null)
        {
            get = new Image(new FileInputStream("src/main/resources/" +src+".png"));
            imageMap.put(src, get);
        }

        return get;
    }

}
