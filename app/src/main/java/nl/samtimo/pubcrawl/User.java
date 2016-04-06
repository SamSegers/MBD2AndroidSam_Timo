package nl.samtimo.pubcrawl;

/**
 * Created by admin on 03-04-16.
 */
public class User {
    private String id;
    private String name;

    public User(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
