package agh.ics.oop;

import org.junit.jupiter.api.Test;

public class MapDirectionTest {
    @Test
    void main() {
        MapDirection[] allenums = {MapDirection.EAST,MapDirection.WEST, MapDirection.SOUTH, MapDirection.NORTH};
        for (MapDirection i : allenums){
            System.out.println("--------------tostr " + i.toString());
            System.out.println("flip " + i.flip().toString());
            System.out.println("next " + i.next().toString());
            System.out.println("prev " + i.previous().toString());
            System.out.println("vector " + i.toUnitVector());
            //^czemu nie dziala .toString()
        }
    }


}
