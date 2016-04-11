package nl.samtimo.pubcrawl;

import java.util.ArrayList;

/**
 * Created by admin on 10-04-16.
 */
public class Participant extends User{
    public Participant(String id, String name, ArrayList<Race> races){
        super(id, name, races);
    }
}
