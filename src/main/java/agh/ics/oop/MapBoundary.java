package agh.ics.oop;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
 //trzeba dodac implementacje jakby to byly obiekty, a nie same pozycje, ale wtedy ciezko z positionchanged wysylajacym sama pozycje
public class MapBoundary  implements IPositionChangeObserver{
    SortedSet<Vector2d> ElementsOnX;    // nazwy pól raczej camelCase'm
    SortedSet<Vector2d> ElementsOnY;

    public MapBoundary()
    {
        ElementsOnX = new TreeSet<>(new Xcomparator());
        ElementsOnY = new TreeSet<>(new Ycomparator());
    }
    public void add(IMapElement x)
    {
        ElementsOnX.add(x.getPosition());
        ElementsOnY.add(x.getPosition());
    }
    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement e) {
        if(oldPosition!=null) {
            ElementsOnX.remove(oldPosition);
            ElementsOnY.remove(oldPosition);
        }

        if(newPosition != null) {
            ElementsOnX.add(newPosition);
            ElementsOnY.add(newPosition);
        }
    }

    public Vector2d topRightBorder(){
        return new Vector2d(ElementsOnX.last().x,ElementsOnY.last().y);}

    public Vector2d bottomLeftBorder(){
        return new Vector2d(ElementsOnX.first().x,ElementsOnY.first().y);}

}

class Xcomparator implements Comparator<Vector2d>
{

    @Override
    public int compare(Vector2d o1, Vector2d o2) {
        if( o1.x > o2.x)
            return 1;   // to nie musi być 1; można na przykład zwracać różnicę x
        if(o1.x < o2.x)
            return -1;
        if(o1.y > o2.y)
            return 1;
        if(o1.y < o2.y)
            return -1;


        return 0;
    }
}

class Ycomparator implements Comparator<Vector2d>
{

    @Override
    public int compare(Vector2d o1, Vector2d o2) {

        if(o1.y > o2.y)
            return 1;
        if(o1.y < o2.y)
            return -1;
        if( o1.x > o2.x)
            return 1;
        if(o1.x < o2.x)
            return -1;


        return 0;
    }
}
