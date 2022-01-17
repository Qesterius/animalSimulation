package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Grass implements IMapElement{//, Comparable<IMapElement>{
    private final Vector2d position;
    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();

    public Grass(Vector2d pos){ position = pos;}

    public Vector2d getPosition() {
        return new Vector2d(position);
    }

    public void cut()
    {
        for (IPositionChangeObserver o: observers) {    // lepiej by użyć dedykowanego observer'a, bo usunięcie z mapy, to nie jest zmiana pozycji
            o.positionChanged(position,null, this);
        }

    }
    @Override
    public boolean isAt(Vector2d pos) {
        return pos.equals(position);
    }

    @Override
    public String getFilename() {
        return "grass";
    }

    @Override
    public String toString() {
        return "*";
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }
/*
    @Override
    public int compareTo(IMapElement o) {
        if (o.getClass() == Animal.class)
            return -1;

        return 0;
    }*/

}
