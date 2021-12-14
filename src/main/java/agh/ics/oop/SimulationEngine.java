package agh.ics.oop;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationEngine implements IEngine, Runnable, IPositionChangeObserver {
    private final MoveDirection[] directions;
    private final IWorldMap map;
    private final ArrayList<Animal> animals = new ArrayList<Animal>();
    private int moveDelay =300;
    private final AtomicBoolean running = new AtomicBoolean(false);

    ArrayList<IPositionChangeObserver> observers = new ArrayList();



    public SimulationEngine(MoveDirection[] directions, IWorldMap map, Vector2d[] initPositions)
    {
        this.directions = directions;
        this.map =map;
        for (Vector2d init:initPositions) {
            Animal i_animal = new Animal(this.map,init);
            if(this.map.place(i_animal)){
                animals.add(i_animal);
                i_animal.addObserver(this);
            }
        }
    }
    public void interrupt(){
        running.set(false);
    }
    @Override
    public void run() {
        running.set(true);
        int counter = 0;
        System.out.println("Thread started.");
        while(counter< directions.length && running.get())
        {
            for (Animal x: animals ) {
                if( running.get()) {
                    x.move(directions[counter]);
                    counter += 1;
                    try {
                        Thread.sleep(moveDelay);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
                else break;
                    // System.out.print(map);
            }
        }
        System.out.println("Simulation Engine stopped working");
            

    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver x: observers ) {
            x.positionChanged(oldPosition,newPosition);
        }
    }


    public void addListener(IPositionChangeObserver listener) {
        observers.add(listener);
    }


    public void removeListener(IPositionChangeObserver listener) {
        observers.add(listener);
    }


}

