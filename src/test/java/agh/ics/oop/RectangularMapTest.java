package agh.ics.oop;

import org.junit.jupiter.api.Test;

public class RectangularMapTest {
    @Test
    public static void main(String[] args) {
        System.out.println("system wystartował");

        MoveDirection[] directions = OptionsParser.parse(args);
        IWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 4)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        System.out.print(map);

        System.out.println("system zakończył działanie");
    }
}
