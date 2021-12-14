package agh.ics.oop;

import javax.lang.model.type.NullType;

public enum MapDirection {
    EAST,
    WEST,
    NORTH,
    SOUTH;

    public String toString()
    {
       String out="";
       switch(this) {
           case EAST -> out = "Wschod";
           case WEST -> out = "Zachod";
           case NORTH -> out = "Polnoc";
           case SOUTH -> out = "Poludnie";
       }
       return out;
    }
    public MapDirection flip(){
        return switch (this) {
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            default -> this;
        };
    }
    public MapDirection next(){
        return switch (this) {
            case EAST -> SOUTH;
            case WEST -> NORTH;
            case NORTH -> EAST;
            case SOUTH -> WEST;
            default -> this;
        };
    }
    public MapDirection previous(){
        return switch (this) {
            case EAST -> NORTH;
            case WEST -> SOUTH;
            case NORTH -> WEST;
            case SOUTH -> EAST;
            default -> this;
        };
    }
    public Vector2d toUnitVector()
    {
        return switch (this) {
            case EAST -> new Vector2d(1, 0);
            case WEST -> new Vector2d(-1, 0);
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            default -> new Vector2d(0, 0);
        };
    }
}
