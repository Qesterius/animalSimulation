package agh.ics.oop;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    protected ArrayList<Vector2d> emptyMapSlots = new ArrayList<>();
    protected ArrayList<Vector2d> emptyJungleSlots = new ArrayList<>();

    public ArrayList<Vector2d> getEmptyMapSlots() {
        return emptyMapSlots;
    }

    public ArrayList<Vector2d> getEmptyJungleSlots() {
        return emptyJungleSlots;
    }

    public Vector2d getJungleBorderNE() {
        return jungleBorderNE;
    }

    public Vector2d getJungleBorderSW() {
        return jungleBorderSW;
    }


    private final Random random = new Random();
    private Vector2d jungleBorderNE;
    private Vector2d jungleBorderSW;

    public GrassField(int width, int height, double jungleRatio)
    {
        super();
        BorderNE = new Vector2d(width-1, height-1);
        BorderSW = new Vector2d(0, 0);

        //jungle setup
        int junglewidth = (int) Math.ceil(width*jungleRatio);
        int jungleheight = (int) Math.ceil(height*jungleRatio);

        jungleBorderSW = new Vector2d( (int) Math.floor((width-junglewidth)/2),(int) Math.floor((height-jungleheight)/2));
        jungleBorderNE = new Vector2d(jungleBorderSW.x+junglewidth,jungleBorderSW.y+jungleheight);



        for(int x=BorderSW.x; x<=BorderNE.x;x++)
            for(int y= BorderSW.y;y<=BorderNE.y;y++)
            {
                Vector2d wektor = new Vector2d(x,y);
                if(wektor.precedes(jungleBorderNE) && wektor.follows(jungleBorderSW))
                    emptyJungleSlots.add(wektor);
                else
                    emptyMapSlots.add(wektor);
            }
    }


    void place(Grass grass) {//g
            grass.addObserver(this);
            addToMap(grass.getPosition(),grass);
    }

    void cutGrass(Grass g)
    {
        g.cut();
    }

    public Grass grassAt(Vector2d position){
        ArrayList<IMapElement> objctsAt = super.objectsAt(position);

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
    public ArrayList<Animal> animalsAt(Vector2d position) {
        ArrayList<IMapElement> objctsAt = super.objectsAt(position);
        ArrayList<Animal> _animals = new ArrayList<>();


        if(objctsAt != null)
        {
           objctsAt.sort(new AnimalGrassComparator() );
            for (IMapElement e: objctsAt) {
                if(e.getClass()==Animal.class)
                    _animals.add((Animal) e);
            }
        }
        return _animals;
    }

    @Override
    public String toString()
    {return super.toString();}

    @Override
    void addToMap(Vector2d key, IMapElement e){
       super.addToMap(key,e);
        if(key.precedes(jungleBorderNE) && key.follows(jungleBorderSW))
            emptyJungleSlots.remove(key);
        else
            emptyMapSlots.remove(key);
    }

    @Override
    void removeFromMap(Vector2d key, IMapElement e )
    {
        super.removeFromMap(key,e);
        if(!objectsOnMap.containsKey(key))
        {
            if(key.precedes(jungleBorderNE) && key.follows(jungleBorderSW))
                emptyJungleSlots.add(key);
            else
                emptyMapSlots.add(key);
        }
    }

    public String DebugOutput(){
        return super.toString();
    }

    public Vector2d randomPositionWithin(){
        return new Vector2d(random.nextInt(BorderNE.x+1- BorderSW.x)+BorderSW.x, random.nextInt(BorderNE.y+1- BorderSW.y)+BorderSW.y);

    }
    public Vector2d randomEmptyPositionWithin(){
        ArrayList<Vector2d> sum = new ArrayList<>(emptyJungleSlots);
        sum.addAll(new ArrayList<>(emptyMapSlots));
        if(sum.size()>0)
            return sum.get(random.nextInt(sum.size()));
        else
            return null;
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
