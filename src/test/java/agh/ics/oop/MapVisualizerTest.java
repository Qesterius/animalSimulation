package agh.ics.oop;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapVisualizerTest {

    @Test
    void test(){
        RectangularMap mapa = new RectangularMap(5,5);
        MoveDirection[] dirs = OptionsParser.parse(new String[]{"f", "b", "r", "l","b","l","b","f"});
        SimulationEngine silnik = new SimulationEngine(dirs,mapa,new Vector2d[]{new Vector2d(0,0), new Vector2d(1,1)});
        System.out.print(mapa.toString());
        silnik.run();

        assertTrue(mapa.isOccupied(new Vector2d(0,1)));
        assertTrue(mapa.isOccupied(new Vector2d(1,0)));
        assertEquals(">",mapa.objectAt(new Vector2d(0,1)).toString());
        assertEquals("v",mapa.objectAt(new Vector2d(1,0)).toString());

        System.out.print(mapa.toString());


    }
}
