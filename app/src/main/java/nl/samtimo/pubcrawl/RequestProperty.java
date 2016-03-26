package nl.samtimo.pubcrawl;

/**
 * Created by admin on 25-03-16.
 */
public class RequestProperty {
    private String name;
    private String value;

    public RequestProperty(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName(){
        return name;
    }

    public String getValue(){
        return value;
    }
}
