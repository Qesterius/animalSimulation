package agh.ics.oop;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GrassFIeldTest {
    @Test
    public static void main(String[] args) {
       String[] arguments = new String[]{"f", "b","r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f"};
        try {
            System.out.println("system wystartował");

            MoveDirection[] directions1 = OptionsParser.parse(arguments);
            GrassField map1 = new GrassField(20);

            Vector2d[] positions1 = {new Vector2d(2, 2), new Vector2d(3, 4)};
            IEngine engine1 = new SimulationEngine(directions1, map1, positions1);
            System.out.print(map1);
            engine1.run();
            System.out.print(map1);
            assertEquals(Animal.class, map1.objectAt(new Vector2d(2, -3)).getClass());
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e);
            return;
        }
        //zwykle run nie dziala
        //debug dziala

    }

}
