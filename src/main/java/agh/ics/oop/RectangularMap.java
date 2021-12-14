package agh.ics.oop;


import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends AbstractWorldMap{

    public RectangularMap(int width, int height)
    {
        super();
        // 0 .. width-1
        // 0.. height-1
        BorderSW = new Vector2d(0,0);
        BorderNE = new Vector2d(width-1,height-1);

    }
/*
    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.lowerLeft(BorderNE).equals(position)
                && position.upperRight(BorderSW).equals(position)
                && !isOccupied(position);
    }*/

    /*@Override
    public boolean place(Animal animal) {
        if(canMoveTo(animal.getPosition()))
        {
            animals.put(animal.getPosition(),animal);
            return true;
        }
        return false;
    }*/

   /* @Override
    public boolean isOccupied(Vector2d position) {
        for (Animal x: animalsOnMap) {
            if (x.getPosition().equals(position)) return true;
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        for (Animal x: animalsOnMap) {
            if (x.getPosition().equals(position)) return x;
        }
        return null;
    }*/


}
