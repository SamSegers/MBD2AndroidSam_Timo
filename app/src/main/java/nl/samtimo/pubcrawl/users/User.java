package nl.samtimo.pubcrawl.users;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.samtimo.pubcrawl.Pub;
import nl.samtimo.pubcrawl.Race;

/**
 * Created by admin on 03-04-16.
 */
public abstract class User {
    private String id;
    private String name;
    private ArrayList<Race> races;

    public User(String id, String name, ArrayList<Race> races) {
        this.id = id;
        this.name = name;
        this.races = races != null ? races : new ArrayList<Race>();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Race> getRaces(){
        return races;
    }

    public boolean isParticipating(Race race){
        for(int i=0;i<races.size();i++){
            if(races.get(i).getId().equals(race.getId())) return true;
        }
        return false;
        //return races.contains(race);
    }

    public int getTagCount(Race race){
        for(int i=0;i<races.size();i++){
            Race currentRace = races.get(i);
            if(currentRace.getId().equals(race.getId())) return currentRace.getTagCount();
        }
        return 0;
    }

    public ArrayList<Pub> getTags(Race race){
        for(int i=0;i<races.size();i++){
            if(races.get(i).getId().equals(race.getId())){
                return races.get(i).getPubs();
            }
        }
        return null;
    }

    public void addTag(Race race, Pub pub){
        for(int i=0;i<races.size();i++){
            Race curRace = races.get(i);
            if(curRace.getId().equals(race.getId())) curRace.setPub(pub, true);
        }
    }

    public void removeTag(Race race, Pub pub){
        for(int i=0;i<races.size();i++){
            Race curRace = races.get(i);
            if(curRace.getId().equals(race.getId())) curRace.setPub(pub, false);
        }
    }
}
