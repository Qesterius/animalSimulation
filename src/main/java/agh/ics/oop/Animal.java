package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement{//, Comparable<IMapElement> {
    private Vector2d position;
    private MapDirection orientation;
    private IWorldMap myMap;
    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();
    private final Integer startingEnergy;


    int numberOfKids = 0;
    public int deathDay = -1;

    public Gene getGenotyp() {
        return genotyp;
    }

    private final Gene genotyp;

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    private Integer energy;

    public Animal(IWorldMap mapa, Vector2d initPos, Gene g, Integer startingEnergy) {
        myMap = mapa;
        position = initPos;
        orientation = MapDirection.NORTH;
        energy = startingEnergy;
        this.startingEnergy = startingEnergy;
        genotyp = g;
    }
    public void eat(Integer energyADD)
    {
           energy+= energyADD;
    }
    public void kill(int day){

        System.out.println("killing "+ this);
        deathDay = day;

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
    public void move()
    {
        move(genotyp.getRandDirection());
    }
    public String printGene(){
        return genotyp.toString();
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
    public String getEnergyFilename() {
        String out = "";
        if(energy>0)
            out = "ENERGY1";
        if(energy>=startingEnergy*(0.25))
            out = "ENERGY2";
        if(energy>=startingEnergy*(0.5))
            out = "ENERGY3";
        if(energy>=startingEnergy*(0.75))
            out = "ENERGY4";
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

}
