package agh.ics.oop;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    protected ArrayList<Vector2d> emptyMapSlots = new ArrayList<>();
    private Random random = new Random();   // to nie może być prywatne?
    private int plantRegion;    // to nie może być prywatne?


    public GrassField(int width, int height, double jungleRatio)
    {
        super();
        BorderNE = new Vector2d(width-1, height-1);
        BorderSW = new Vector2d(0, 0);

        for(int x=BorderSW.x; x<=BorderNE.x;x++)
            for(int y= BorderSW.y;y<=BorderNE.y;y++)
                emptyMapSlots.add(new Vector2d(x,y));
    }


    private void plantGrasses(int grassQuantity)
    {
    //toworkm
    }


    private void place(Grass grass) {//g
            addToMap(grass.getPosition(),grass);
    }

    /*public void place(Animal animal)
    {
        addToMap(animal.getPosition(),animal);
    }*///w abstrakcie jest wysstarczajaco napisane

    /*public boolean isOccupied(Vector2d position)  NIEPOTRZEBNE
    {
        boolean isAnimHere = super.isOccupied(position);

        if(!isAnimHere)
            for(Map.Entry<Vector2d, Grass> x : grasses.entrySet()){
                if(x.getValue().getPosition().equals(position)) return true;
            }
        return isAnimHere;
    }*/

    public Grass grassAt(Vector2d position){
        ArrayList<IMapElement> objctsAt = super.objectsAt(position);
        IMapElement first = null;

        if(objctsAt != null)
        {
            for (IMapElement e: objctsAt) {
                if(e.getClass() ==Grass.class)
                    return (Grass) e;
            }
        }

        return null;
    }

    public IMapElement objectAt(Vector2d position) {
        ArrayList<IMapElement> objctsAt = super.objectsAt(position);
        IMapElement first = null;

        if(objctsAt != null)
        {
           objctsAt.sort(new AnimalGrassComparator() );
           first = objctsAt.get(objctsAt.size()-1);
        }
        return first;
    }


    @Override
    public String toString()
    {
        /*for (Map.Entry<Vector2d, Animal> x : animals.entrySet()) {

            BorderNE = BorderNE.upperRight(x.getValue().getPosition());
            BorderSW = BorderSW.lowerLeft(x.getValue().getPosition());
        }*/

        return super.toString();
    }

    /*private void cutGrass(Vector2d grassPos){  // inaczej ma dzialac
        Grass g = grassAt(grassPos);
        if(g!=null) {
            System.out.println("OMOM, grass munched at" +grassPos);
            g.cut();
            grasses.remove(grassPos);
            plantGrasses(1);
        }
    }*/

    @Override
    void addToMap(Vector2d key, IMapElement e){
       super.addToMap(key,e);
       emptyMapSlots.remove(key);
    }
    @Override
    void removeFromMap(Vector2d key, IMapElement e )
    {
        super.removeFromMap(key,e);
        if(!objectsOnMap.containsKey(key))
        {
            emptyMapSlots.add(key);
        }
    }

    public String DebugOutput(){
        return super.toString();
    }
}

class AnimalGrassComparator implements Comparator<IMapElement>{

    @Override
    public int compare(IMapElement o1, IMapElement o2) {
        if(o1.getClass() ==Animal.class)
        {

            if( o2.getClass() == Grass.class)
                return 1;
            else if(o2.getClass() == Animal.class)
                return Integer.compare(((Animal) o1).getEnergy(),((Animal) o2).getEnergy());

            return 0;
        }
        else if (o2.getClass() ==Animal.class){ return -1;}

        return 0;
    }
}
