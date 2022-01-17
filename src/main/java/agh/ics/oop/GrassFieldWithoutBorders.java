package agh.ics.oop;

public class GrassFieldWithoutBorders extends GrassField{
    public GrassFieldWithoutBorders(int width, int height, double jungleRatio) {
        super(width, height, jungleRatio);
    }

    @Override
    public Vector2d processPossibleMove(Vector2d from,Vector2d to) {//g // niezbyt czytelna nazwa
        if(isPositionWithinMapBorders(to))
            return to;
        else
        {
            return new Vector2d((to.x+BorderNE.x)%(BorderNE.x+1),(to.y+BorderNE.y)%(BorderNE.y+1)); // skąd to +1 w "mianowniku"?
            // czy ten wzór zadziała, jeśli BorderSW będzie inne niż (0,0)?
        }
    }
}
