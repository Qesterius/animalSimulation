package agh.ics.oop;

import java.util.Locale;
import java.util.ArrayList;

public class OptionsParser {

    public static MoveDirection[] parse(String[] args) throws IllegalArgumentException{

        ArrayList<MoveDirection> out = new ArrayList<MoveDirection>();

        for (int i = 0; i < args.length; i++)
        {
            switch (args[i].toLowerCase(Locale.ROOT)) {
                case "f","forward" ->  { out.add(MoveDirection.FORWARD);}
                case "b","backward" -> {out.add(MoveDirection.BACKWARD);}
                case "r","right" -> {out.add(MoveDirection.ROT45);}
                case "l","left" -> {out.add(MoveDirection.ROT315);}
                default -> { throw new IllegalArgumentException(args[i] + "is not a valid argument for move, system shutting down");}
            }
        }
        MoveDirection[] result = new MoveDirection[out.size()];
        for (int i=0;i<out.size();i++) {
            result[i] = out.get(i);

        }
        return result;
    }

}
