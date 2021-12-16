package agh.ics.oop;

import java.util.*;

public class Gene {
    private ArrayList<MoveDirection> geneCode;
    private Random rand;

    public Gene(MoveDirection[] tab)
    {
        geneCode = new ArrayList(32);
        if (tab.length!=32)
            throw new IllegalArgumentException("Incorrect argument size for Gene constructor");

        geneCode.addAll(List.of(tab));
        Collections.sort(geneCode);
        rand = new Random();
    }

    public Gene(Gene anim1,int energy1, Gene anim2,int energy2)
    {
        geneCode = new ArrayList(32);
        rand = new Random();
        boolean isOneLeft = (rand.nextInt(2) ==1);
        int splitPoint;
        if(isOneLeft)
        {
            splitPoint = (int) Math.floor((energy1 /(energy1 +energy2))*32);
            ArrayList<MoveDirection> left = anim1.getGeneCode();
            ArrayList<MoveDirection> right = anim2.getGeneCode();
            for(int i=0; i<splitPoint;i++)
            {
                geneCode.add(left.get(i));
            }
            for(int i=splitPoint; i<32;i++)
            {
                geneCode.add(right.get(i));
            }
        }
        else{
            splitPoint = (int) Math.floor((energy2 /(energy1 +energy2))*32);
            ArrayList<MoveDirection> left = anim2.getGeneCode();
            ArrayList<MoveDirection> right = anim1.getGeneCode();
            for(int i=0; i<splitPoint;i++)
            {
                geneCode.add(left.get(i));
            }
            for(int i=splitPoint; i<32;i++) {
                geneCode.add(right.get(i));
            }
        }

        Collections.sort(geneCode);
    }

    public ArrayList<MoveDirection> getGeneCode() {
        return new ArrayList<>(geneCode);
    }

    public MoveDirection getRandDirection() {
        return geneCode.get(rand.nextInt(32));
    }
}
