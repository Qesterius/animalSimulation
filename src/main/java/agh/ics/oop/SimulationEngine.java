package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationEngine implements IEngine, Runnable, IPositionChangeObserver {
    private GrassField map=null;
    private ArrayList<Animal> animals = new ArrayList<Animal>();
    private ArrayList<Animal> deadAnimals = new ArrayList<Animal>();
    private ArrayList<Vector2d> grassesPos = new ArrayList<>();

    public ArrayList<Animal> childrenOfInspected = new ArrayList<>();
    public Animal inspected;
    public int directChildren =0;

    Random random;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(true);
    AtomicBoolean inspecting= new AtomicBoolean(false);
    App renderer;
    int day = 1;


    ///SETTINGS
    private int moveDelay =300;
    int eatValue =1;
    int minCoopulationEnergy = 5;
    int startingEnergy = 20;
    int moveEnergy;
    boolean isMap1;
    boolean isMagical;
    int magicCount;

    public SimulationEngine( GrassField _map, int _eatValue, int numOfAnims, int _startingEnergy, int _moveEnergy, boolean _isMap1, boolean _isMagical)
    {
        eatValue = _eatValue;
        map = _map;
        startingEnergy = _startingEnergy;
        minCoopulationEnergy = (int) Math.floor( startingEnergy/2);
        random = new Random();
        moveEnergy = _moveEnergy;
        isMap1 = _isMap1;
        isMagical = _isMagical;
        magicCount = 0;

        for (int i = 0; i < numOfAnims; i++) {
            addAnimal(new Animal(map,map.randomPositionWithin(),new Gene(),startingEnergy));
        }
    }

    public void interrupt(){
        paused.set(!paused.get());
    }
    @Override
    public void run() {
        running.set(true);

        System.out.println("Thread started.");
        while(running.get())
        {
                    if(!paused.get())
                    {
                        removedead();
                        plantGrasses();
                        move();
                        eating();
                        coopulation();
                        if(isMagical && magicCount<3)
                            magic();
                        day+=1;

                        renderer.drawCall(isMap1);
                        if(inspecting.get())
                            sendInspection();
                    }
                    try {
                        Thread.sleep(moveDelay);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        return;
                    }


        }
        System.out.println("Simulation Engine stopped working");
            

    }

    private void magic() {
        if(animals.size() == 5)
        {
            magicCount+=1;
            String animalsOut = "";
            for (Animal a: new ArrayList<>(animals) ) {
                Vector2d rand = map.randomEmptyPositionWithin();

                if(rand!=null)
                {
                    animalsOut += "Animal at"+(rand.toString())+"\n";
                    addAnimal(new Animal(map,rand,a.getGenotyp(),a.getEnergy()));
                }
            }
            renderer.sendMessage(isMap1,"Magic Happened\n New Animals emerged:\n" +animalsOut);
        }

    }

    private void removedead() {
        for (Animal a:new ArrayList<>(animals)) {
            if(a.getEnergy()<1)
            {

                a.kill(day);
                positionChanged(a.getPosition(),null,a);
                map.positionChanged(a.getPosition(),null,a);
                deadAnimals.add(a);
            }
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement e) {
        if(newPosition == null)
        {
            if(e.getClass() == Animal.class)
                animals.remove(e);
            if(e.getClass() == Grass.class)
                grassesPos.remove(((Grass) e).position);
        }

    }

    private void eating()
    {

        for(Vector2d grassPos: new ArrayList<>(grassesPos))
        {
            Grass g = map.grassAt(grassPos);

            ArrayList<Animal> animalsHere = map.animalsAt(grassPos);

            if(animalsHere.size()>0)
            {
                //System.out.println(g);
                animalsHere.sort(new AnimalGrassComparator());
                int maxEnergy = animalsHere.get(0).getEnergy();
                animalsHere.removeIf(a -> a.getEnergy()< maxEnergy);

                int spliting = animalsHere.size();
                for (Animal a:animalsHere) {
                    a.eat(eatValue/spliting);
                }
                map.cutGrass(g);
                grassesPos.remove(g.getPosition());
            }
        }
    }
    private void move(){
        for (Animal x: animals) {
            x.move();
            x.setEnergy(x.getEnergy()-moveEnergy);
        }
    }

    private void coopulation()
    {
        for (Vector2d pos: new ArrayList<>(map.getKeySet())) {
            ArrayList<Animal> objectsAt = map.animalsAt(pos);

            if(objectsAt.size()>1)
            {
                Animal parent1 = (Animal)objectsAt.get(0);
                Animal parent2 = (Animal)objectsAt.get(1);

                if( parent1.getEnergy() > minCoopulationEnergy && parent2.getEnergy()>minCoopulationEnergy)
                {
                    int lpenergy = (int) (parent1.getEnergy()*0.25);
                    parent1.setEnergy(parent1.getEnergy()-lpenergy);

                    int rpenergy = (int) (parent2.getEnergy()*0.25);
                    parent2.setEnergy(parent2.getEnergy()-rpenergy);

                    Animal dziecko = new Animal(map,
                                                pos,
                                                new Gene
                                                        (
                                                                parent1.getGenotyp(),
                                                                parent1.getEnergy(),
                                                                parent2.getGenotyp(),
                                                                parent2.getEnergy()
                                                        ),  lpenergy+rpenergy
                                                );
                    addAnimal(dziecko);


                    if(inspecting.get())
                    {
                        if(parent1 == inspected || parent2 == inspected)
                        {
                            childrenOfInspected.add(dziecko);
                            directChildren+=1;
                        }
                        else if(childrenOfInspected.contains(parent1) || childrenOfInspected.contains(parent2) )
                        {
                            childrenOfInspected.add(dziecko);
                        }
                    }
                }
            }
        }

    }

    public void plantGrasses()
    {
        //junglePlant
        ArrayList<Vector2d> emptyJungleSlots = map.getEmptyJungleSlots();
        if(!emptyJungleSlots.isEmpty())
             {
                 Vector2d _empty = emptyJungleSlots.get(random.nextInt(emptyJungleSlots.size()));
                 grassesPos.add(_empty);
                 map.place(new Grass(_empty));
             }
        //mapPlant
        ArrayList<Vector2d> emptyMapSlots = map.getEmptyMapSlots();
        if(!emptyMapSlots.isEmpty())
            {
                Vector2d _empty = emptyMapSlots.get(random.nextInt(emptyMapSlots.size()));
                grassesPos.add(_empty);
                map.place(new Grass(_empty));
            }
    }

    public void addListener(App application) {
        renderer = application;
    }

    private void addAnimal(Animal a)
    {
        map.place(a);
        animals.add(a);
    }
    public boolean requestInspecting(Vector2d position)
    {
        if(paused.get()) {
            inspecting.set(true);
            directChildren =0;
            IMapElement tmp_inspected = map.objectAt(position);
            if(tmp_inspected == null)
                return false;
            if(tmp_inspected.getClass() != Animal.class )
                return false;

            inspected = (Animal) tmp_inspected ;
            childrenOfInspected = new ArrayList<>();
        }
        return false;
    }
    private void sendInspection()
    {
        String output =
                    "Animal:"+inspected.getPosition()+"\n"+
                    "Number of direct children:"+directChildren+"\n"+
                    "Number of all children:"+childrenOfInspected.size()+"\n"+
                    "Date of death"+ (inspected.deathDay== -1? "not yet :))" : inspected.deathDay)+"\n";
        renderer.sendInspection(isMap1, output);
    }

    private void sendChartData()
    {

    }

}

