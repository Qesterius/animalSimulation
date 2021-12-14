package agh.ics.oop;

import java.util.*;

public class GrassField extends AbstractWorldMap{

    private Random random = new Random();   // to nie może być prywatne?
    private Map<Vector2d,Grass> grasses = new LinkedHashMap();   // to nie ma prawa być publiczne
    private MapBoundary mapBoundary = new MapBoundary();
    private int plantRegion;    // to nie może być prywatne?

    public GrassField(int grassQuantity)
    {
        super();

        //(0,0)  -> (sqrt(n*10), sqrt(n*10))
        plantRegion = (int)Math.sqrt(grassQuantity*10);
        BorderNE = new Vector2d(plantRegion, plantRegion);
        BorderSW = new Vector2d(0, 0);
        plantGrasses(grassQuantity);
    }
    private void plantGrasses(int grassQuantity)
    {
        int success = 0;
        while(success<grassQuantity)
        {
            Vector2d rand = new Vector2d(random.ints(0,plantRegion).findAny().getAsInt(), random.ints(0,plantRegion).findAny().getAsInt());
            if(place(new Grass(rand))){  success++;}
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if( isOccupied(position) )
        {
            if(objectAt(position).getClass() == Grass.class)
                return true;
            return false;
        }
        return true;
    }

    public boolean place(Grass grass) { // czy public?
        if(!isOccupied(grass.position))
        {
            grasses.put(grass.getPosition(),grass);
            grass.addObserver(mapBoundary);
            mapBoundary.add(grass);
            return true;
        }


        return false;
    }
    public boolean place(Animal animal)
    {
        boolean placed = super.place(animal);
        if( placed )
        {
            animal.addObserver(mapBoundary);
            mapBoundary.add(animal);
            cutGrass(animal.getPosition());
        }
        return placed;
    }

    public boolean isOccupied(Vector2d position)
    {
        boolean isAnimHere = super.isOccupied(position);

        if(!isAnimHere)
            for(Map.Entry<Vector2d, Grass> x : grasses.entrySet()){
                if(x.getValue().getPosition().equals(position)) return true;
            }
        return isAnimHere;
    }

    public Grass grassAt(Vector2d position){
        for (Map.Entry<Vector2d, Grass> x : grasses.entrySet()) {
            if (x.getValue().getPosition().equals(position)) return x.getValue();
        }
        return null;
    }

    public IMapElement objectAt(Vector2d position) {
        IMapElement obAt = super.objectAt(position);
        if(obAt == null)
        {
            for (Map.Entry<Vector2d, Grass> x : grasses.entrySet())
            {
                if (x.getValue().getPosition().equals(position)) return x.getValue();
            }
        }
        return obAt;
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
    private void cutGrass(Vector2d grassPos){  // czy public?  a gdyby jako parametr przyjmować pozycję?
        Grass g = grassAt(grassPos);
        if(g!=null) {
            System.out.println("OMOM, grass munched at" +grassPos);
            g.cut();
            grasses.remove(grassPos);
            plantGrasses(1);
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        super.positionChanged(oldPosition,newPosition);

        cutGrass(newPosition);
    }



    @Override
    public Vector2d getBorderNE() {
        BorderNE = mapBoundary.topRightBorder();
        return super.getBorderNE();
    }

    @Override
    public Vector2d getBorderSW() {
        BorderSW = mapBoundary.bottomLeftBorder();
        return super.getBorderSW();
    }
}
