package nl.samtimo.pubcrawl.users;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.samtimo.pubcrawl.Pub;
import nl.samtimo.pubcrawl.Race;
import nl.samtimo.pubcrawl.Util;
import nl.samtimo.pubcrawl.request.Request;
import nl.samtimo.pubcrawl.request.RequestMethod;
import nl.samtimo.pubcrawl.request.RequestTask;

/**
 * Created by admin on 10-04-16.
 */
public class CurrentUser extends User {
    private ArrayList<Pub> pubs;

    public CurrentUser(String id, String name, ArrayList<Race> races) {
        super(id, name, races);

        pubs = new ArrayList<>();

        Request request = new Request(RequestMethod.GET, "users/pubs", null, null);
        new RequestTask(this).execute(request);
    }

    public void initPubs(String result){
        try{
            JSONObject jResult = new JSONObject(result);
            JSONArray jPubs = jResult.getJSONArray("pub");
            for(int i=0;i<jPubs.length();i++){
                JSONObject jPub = jPubs.getJSONObject(i);
                pubs.add(new Pub(Util.getJsonString(jPub, "id"), Util.getJsonString(jPub, "name"), false, null));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean isInCollection(Pub pub){
        for(int i=0;i<pubs.size();i++)
            if(pubs.get(i).getId().equals(pub.getId())) return true;
        return false;
    }

    public void addPub(Pub pub){
        for(int i=0;i<pubs.size();i++){
            if(pubs.get(i).getId().equals(pub.getId())) return;
        }
        pubs.add(pub);
    }

    public void removePub(Pub pub){
        System.out.println("removePub");
        for(int i=0;i<pubs.size();i++){
            System.out.println("removePub "+pubs.get(i).getId().equals(pub.getId()));
            if(pubs.get(i).getId().equals(pub.getId())){
                pubs.remove(i);
                return;
            }
        }
    }
}
