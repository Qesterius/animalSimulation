package agh.ics.oop;

public interface IMapElement {

    public Vector2d getPosition();
    public boolean isAt(Vector2d pos);

    public String getFilename();    // lepiej to przerzucić do GUI

    @Override
    public String toString() ;
}
