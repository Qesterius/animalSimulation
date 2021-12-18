package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement{//, Comparable<IMapElement> {
    private Vector2d position;
    private MapDirection orientation;
    private IWorldMap myMap;
    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();
    private Gene genotyp;
    private Integer energy;





    public Animal(IWorldMap mapa, Vector2d initPos, Gene g) {
        myMap = mapa;
        position = initPos;
        orientation = MapDirection.NORTH;
        genotyp = g;
    }

    @Override
    public Vector2d getPosition() {
        return new Vector2d(position);
    }


    public MapDirection getOrientation() {
        return orientation;
    }


    public void move(MoveDirection dir) {
        Vector2d possibleMove = getPosition();

        switch (dir) {
            case FORWARD -> possibleMove = position.add(orientation.toUnitVector());
            case ROT45 -> orientation = orientation.next();
            case ROT90 -> orientation = orientation.next().next();
            case ROT135 -> orientation = orientation.next().next().next();
            case BACKWARD -> possibleMove = position.subtract(orientation.toUnitVector());
            case ROT225 -> orientation = orientation.previous().previous().previous();
            case ROT270 -> orientation = orientation.previous().previous();
            case ROT315 -> orientation = orientation.previous();
        }
        possibleMove = myMap.processPossibleMove(position,possibleMove);
        if ( position != possibleMove ) {
            Vector2d lastpos = getPosition();
            position = possibleMove;
            positionChanged(lastpos, position);
        }
    }


    @Override
    public String toString() {
        String out = "";
        switch(orientation) {
            case EAST -> out = ">";
            case WEST -> out = "<";
            case NORTH -> out = "^";
            case SOUTH -> out = "v";
            case SOUTH_WEST -> out = "./";
            case WEST_NORTH -> out = "\'\\";
            case NORTH_EAST -> out = "/\'";
            case EAST_SOUTH -> out = "\\.";
        }
        return out;
    }

    public boolean isAt(Vector2d pos) {
        return pos.equals(position);
    }

    @Override
    public String getFilename() {
        String out = "";
        switch(orientation) {
            case EAST -> out = "R";
            case WEST -> out = "L";
            case NORTH -> out = "U";
            case SOUTH -> out = "D";
            case SOUTH_WEST -> out = "LD";
            case WEST_NORTH -> out = "LU";
            case NORTH_EAST -> out = "RU";
            case EAST_SOUTH -> out = "RD";
        }
        return out;
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    private void positionChanged(Vector2d from, Vector2d to) {
        for (IPositionChangeObserver obs : observers) {
            obs.positionChanged(from, to, this );
        }

    }
    public Integer getEnergy(){ return energy;}

/*
    @Override
    public int compareTo(IMapElement o) {
        if( o.getClass() == Grass.class)
            return 1;
        else if(o.getClass() == Animal.class)
            return Integer.compare(this.energy,((Animal) o).energy);

        return 0;
    }*/
}
