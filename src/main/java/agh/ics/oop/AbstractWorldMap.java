package agh.ics.oop;

import java.util.Map;
import java.util.LinkedHashMap;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    //protected List<Animal> animals = new ArrayList<>();
    protected Map<Vector2d, Animal> animals = new LinkedHashMap<>();
    protected Vector2d BorderNE;
    protected Vector2d BorderSW;
    protected MapVisualizer visualizer; // to pole może być finalne i prywatne

    public AbstractWorldMap() {
        visualizer = new MapVisualizer(this);

    }

    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            animal.addObserver(this);
            animals.put(animal.getPosition(), animal);
            return true;
        }
        throw new IllegalArgumentException(animal.getPosition() + " is illegal starting point for animal");
    }

    public boolean canMoveTo(Vector2d position) {
        return position.lowerLeft(BorderNE).equals(position)
                && position.upperRight(BorderSW).equals(position)
                && !isOccupied(position);
    }

    public boolean isOccupied(Vector2d position) {

        for (Map.Entry<Vector2d, Animal> x : animals.entrySet()) {
            if (x.getValue().getPosition().equals(position)) return true;
        }
        return false;
    }

    public IMapElement objectAt(Vector2d position) {
        for (Map.Entry<Vector2d, Animal> x : animals.entrySet()) {
            if (x.getValue().getPosition().equals(position)) return x.getValue();
        }
        return null;
    }
    //public void moved(Animal anim){};

    public String toString() {
        return visualizer.draw(getBorderSW(), getBorderNE());
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        Animal a = animals.remove(oldPosition);
        animals.put(newPosition, a);

        return;
    }

    public Vector2d getBorderNE() {
        return BorderNE;
    }

    public Vector2d getBorderSW() {
        return BorderSW;
    }
}
