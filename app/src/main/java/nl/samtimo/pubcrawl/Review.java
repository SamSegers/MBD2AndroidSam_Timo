package nl.samtimo.pubcrawl;

/**
 * Created by admin on 07-04-16.
 */
public class Review {
    private String name;
    private String body;

    public Review(String name, String body){
        this.name = name;
        this.body = body;
    }

    public String getName(){
        return name;
    }

    public String getBody(){
        return body;
    }
}
