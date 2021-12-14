package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement {
    private Vector2d position;
    private MapDirection orientation;
    private IWorldMap myMap;
    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();

    /*
    private void guardMapPos()
    {
        position = position.lowerLeft(BorderNE);
        position = position.upperRight(BorderSW);
    }
    */
    /*
    public Animal() {
        position = new Vector2d(2,2);
        orientation = MapDirection.NORTH;
    }*/
    /*public Animal(IWorldMap mapa)
    {
        myMap = mapa;
        orientation = MapDirection.NORTH;
    }*/
    public Animal(IWorldMap mapa, Vector2d initPos) {
        myMap = mapa;
        position = initPos;
        orientation = MapDirection.NORTH;
    }

    @Override
    public Vector2d getPosition() {
        return new Vector2d(position);
    }


    public MapDirection getOrientation() {
        return orientation;
    }

    /*public void move(ArrayList<MoveDirection> dir)
    {

        for(MoveDirection i : dir) {
            switch (i) {
                case LEFT -> orientation = orientation.previous();
                case RIGHT -> orientation =orientation.next();
                case FORWARD -> position = position.add(orientation.toUnitVector());
                case BACKWARD -> position = position.subtract(orientation.toUnitVector());
            }
            guardMapPos();
        }
    }*/
    public void move(MoveDirection dir) {
        Vector2d possibleMove = getPosition();

        switch (dir) {
            case LEFT -> orientation = orientation.previous();
            case RIGHT -> orientation = orientation.next();
            case FORWARD -> possibleMove = position.add(orientation.toUnitVector());
            case BACKWARD -> possibleMove = position.subtract(orientation.toUnitVector());
        }

        if (myMap.canMoveTo(possibleMove)) {
            Vector2d lastpos = getPosition();
            position = possibleMove;
            positionChanged(lastpos, position);
        }
    }

    public void move(ArrayList<MoveDirection> moveDirections) {
        for (MoveDirection dir : moveDirections
        ) {
            move(dir);
        }
    }

    public void move(MoveDirection[] moveDirections) {
        for (MoveDirection dir : moveDirections
        ) {
            move(dir);
        }
    }

    @Override
    public String toString() {
        String orientationVisual = "";
        switch (orientation) {
            case NORTH -> orientationVisual = "^";
            case SOUTH -> orientationVisual = "v";
            case EAST -> orientationVisual = ">";
            case WEST -> orientationVisual = "<";
        }
        return orientationVisual;
    }

    public boolean isAt(Vector2d pos) {
        return pos.equals(position);
    }

    @Override
    public String getFilename() {
        String orientationVisual = "";
        switch (orientation) {
            case NORTH -> orientationVisual = "up";
            case SOUTH -> orientationVisual = "down";
            case EAST -> orientationVisual = "right";
            case WEST -> orientationVisual = "left";
        }
        return orientationVisual;
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    private void positionChanged(Vector2d from, Vector2d to) {
        for (IPositionChangeObserver obs : observers) {
            obs.positionChanged(from, to);
        }

    }
}
