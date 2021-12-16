package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    //protected List<Animal> animals = new ArrayList<>();

    protected Map<Vector2d, ArrayList<IMapElement>> objectsOnMap = new LinkedHashMap<>();
    protected Vector2d BorderNE;
    protected Vector2d BorderSW;
    final private MapVisualizer visualizer; // to pole może być finalne i prywatne

    public AbstractWorldMap() {
        visualizer = new MapVisualizer(this);
    }

    void addToMap(Vector2d key, IMapElement e){
        if(!objectsOnMap.containsKey(key))
            objectsOnMap.put(key, new ArrayList<>());

        objectsOnMap.get(key).add(e);
    }
    void removeFromMap(Vector2d key, IMapElement e )
    {
        objectsOnMap.get(key).remove(e);
        if(objectsOnMap.get(key).size() == 0)
            objectsOnMap.remove(key);
    }

    public boolean place(Animal animal) {
        if (isPositionWithinMapBorders(animal.getPosition())) {
            animal.addObserver(this);
            addToMap(animal.getPosition(), animal);
            return true;
        }
        throw new IllegalArgumentException(animal.getPosition() + " is illegal starting point for animal");
    }

    public boolean isPositionWithinMapBorders(Vector2d pos){
        return pos.lowerLeft(BorderNE).equals(pos)
                && pos.upperRight(BorderSW).equals(pos);
    }

    public Vector2d processPossibleMove(Vector2d from,Vector2d to) {
        return isPositionWithinMapBorders(to) ? from : to;
    }

    public boolean isOccupied(Vector2d position) {

        /*for (Map.Entry<Vector2d, ArrayList<Animal>> x : animals.entrySet()) {
            for ( Animal y: x.getValue()) {
                if (y.getPosition().equals(position)) return true;
            }
        }*/
        return objectsOnMap.get(position) != null;
    }

    public ArrayList<IMapElement> objectsAt(Vector2d position) {

        return objectsOnMap.get(position);
    }

    public String toString() {
        return visualizer.draw(getBorderSW(), getBorderNE());
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement e) {
        if(oldPosition!=null)
            removeFromMap(oldPosition,e);

        if(newPosition!=null)
            addToMap(newPosition, e);
    }

    public Vector2d getBorderNE() {
        return new Vector2d(BorderNE);
    }

    public Vector2d getBorderSW() {
        return new Vector2d(BorderSW);
    }
}
