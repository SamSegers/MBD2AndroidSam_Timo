package nl.samtimo.pubcrawl.users;

import java.util.ArrayList;

import nl.samtimo.pubcrawl.Race;
import nl.samtimo.pubcrawl.users.User;

/**
 * Created by admin on 10-04-16.
 */
public class Participant extends User {
    public Participant(String id, String name, ArrayList<Race> races){
        super(id, name, races);
    }
}
