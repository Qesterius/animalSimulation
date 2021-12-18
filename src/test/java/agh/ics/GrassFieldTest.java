package agh.ics;

import agh.ics.oop.Animal;
import agh.ics.oop.Gene;
import agh.ics.oop.GrassField;
import agh.ics.oop.Vector2d;
import org.junit.jupiter.api.Test;

public class GrassFieldTest {
@Test
    void test(){
        GrassField mapa = new GrassField(10,10,1/2);
        Vector2d ani1 =new Vector2d(0,0);
        System.out.println( ani1.lowerLeft(BorderNE).equals(pos)
            && pos.upperRight(BorderSW).equals(pos)
        mapa.place(new Animal(mapa, new Vector2d(0,0), new Gene()));

        System.out.println(mapa.DebugOutput());
    }
}
