package agh.ics.oop;

public enum MapDirection {
    EAST,
    EAST_SOUTH,
    SOUTH,
    SOUTH_WEST,
    WEST,
    WEST_NORTH,
    NORTH,
    NORTH_EAST;

    public String toString()
    {
       String out="";
       switch(this) {
           case EAST -> out = "Wschod";
           case WEST -> out = "Zachod";
           case NORTH -> out = "Polnoc";
           case SOUTH -> out = "Poludnie";
           case SOUTH_WEST -> out = "Poludniowy-Zachod";
           case WEST_NORTH -> out = "Polnocny-Zachod";
           case NORTH_EAST -> out = "Polnocny-Wschod";
           case EAST_SOUTH -> out = "Poludniowy-Wschod";
       }
       return out;
    }
    public MapDirection flip(){
        return switch (this) {
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case SOUTH_WEST -> NORTH_EAST;
            case WEST_NORTH -> EAST_SOUTH;
            case NORTH_EAST -> SOUTH_WEST;
            case EAST_SOUTH -> WEST_NORTH;
        };
    }
    public MapDirection next(){
        return switch (this) {
            case EAST -> EAST_SOUTH;
            case EAST_SOUTH -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> WEST_NORTH;
            case WEST_NORTH -> NORTH;
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
        };
    }
    public MapDirection previous(){
        return switch (this) {
            case EAST -> NORTH_EAST;    // przy tej liczbie kierunków już się bardzo opłaca użyć values i ordinal
            case EAST_SOUTH -> EAST;
            case SOUTH -> EAST_SOUTH;
            case SOUTH_WEST -> SOUTH;
            case WEST -> SOUTH_WEST;
            case WEST_NORTH -> WEST;
            case NORTH -> WEST_NORTH;
            case NORTH_EAST -> NORTH;
            default -> this;    // tylko wyjątek w takiej sytuacji
        };
    }
    public Vector2d toUnitVector()
    {
        return switch (this) {
            case EAST -> new Vector2d(1, 0);    // nowy wektor co wywołanie
            case EAST_SOUTH -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case WEST_NORTH -> new Vector2d(-1, 1);
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
        };
    }
}
