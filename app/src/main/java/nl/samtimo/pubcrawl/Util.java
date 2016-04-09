package nl.samtimo.pubcrawl;

import org.json.JSONObject;

/**
 * Created by admin on 09-04-16.
 */
public class Util {
    public static String getJsonString(JSONObject object, String name){
        try{
            return object.has(name)?object.getString(name):null;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
