package agh.ics.oop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    @Test
    void test(){

        RectangularMap map = new RectangularMap(10,10);
        Animal tesla_dog = new Animal(map,new Vector2d(2,2));
        String[] input = {"forward","right","r","forward","backward"};
        MoveDirection[] answer = {MoveDirection.FORWARD,MoveDirection.RIGHT,MoveDirection.RIGHT,MoveDirection.FORWARD,MoveDirection.BACKWARD};
        MoveDirection[] parsed = OptionsParser.parse(input);
        String[] badarg = {"a","b"};

        Exception a = assertThrows(IllegalArgumentException.class,() -> {OptionsParser.parse(badarg);});
        System.out.println(a.getMessage());
        assertEquals("ais not a valid argument for move, system shutting down", a.getMessage());
        assertArrayEquals(answer,parsed);


        tesla_dog.move(parsed);



        assertEquals(MapDirection.SOUTH,tesla_dog.getOrientation());
        assertEquals(new Vector2d(2,3),tesla_dog.getPosition());

        tesla_dog.move(new MoveDirection[]{MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.RIGHT, MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.FORWARD});
        assertEquals(new Vector2d(0,0), tesla_dog.getPosition());


    }
}
