package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Vector2DTest {
    @Test
    void main() {
        Vector2d[] someVectors = {
                new Vector2d(12,43),
                new Vector2d(-2,1),
                new Vector2d(100,-4),
                new Vector2d(0,3),
                new Vector2d(0,-103),
                new Vector2d(0,0)
        };

        for (Vector2d i : someVectors){
            /*System.out.println("--------------tostr " + i.toString());
            System.out.println("upperRight 0,0" + i.upperRight( new Vector2d(0,0)).toString());
            System.out.println("lowerLeft 0,0" + i.lowerLeft( new Vector2d(0,0)).toString());
            System.out.println("add 2,2" + i.add(new Vector2d(2,2)).toString());
            System.out.println("subtract 2,2" + i.subtract(new Vector2d(2,2)).toString());
            System.out.println("precedes 2,2" + i.precedes(new Vector2d(2,2)));
            System.out.println("follows 2,2" + i.follows(new Vector2d(2,2)));
            System.out.println("opposite " + i.opposite().toString());
            System.out.println("equals same " + i.equals(new Vector2d(i.x,i.y)));
            System.out.println("equals notsame " + i.equals(new Vector2d(i.x -1,i.y+2)));
            */

            assertEquals(new Vector2d((i.x-2),(i.y-2)),i.subtract(new Vector2d(2,2)));
            assertFalse(i.precedes(new Vector2d(-1000,-20000)));
            assertTrue(i.follows(new Vector2d(-1000,-20000)));
            assertEquals(i.opposite(), new Vector2d(-i.x,-i.y));
            assertTrue(i.equals(new Vector2d(i.x,i.y)));
            assertFalse(i.equals(new Vector2d(i.x -1,i.y+2)));

        }

    }

}
