package nl.samtimo.pubcrawl;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 31-03-16.
 */
public class Race {
    private String id;
    private String name;
    private ArrayList<Pub> pubs;
    //private ArrayList<User> users;
    private Date startDate;
    private Date endDate;
    private Bitmap image;

    public Race(String id, String name){
        this.id = id;
        this.name = name;
        this.pubs = new ArrayList<>();
    }

    public Race(String id, String name, ArrayList<Pub> pubs){
        this.id = id;
        this.name = name;
        this.pubs = pubs!=null?pubs:new ArrayList<Pub>();
    }

    public Race(String id, String name, ArrayList<Pub> pubs, Date startDate, Date endDate){
        this.id = id;
        this.name = name;
        this.pubs = pubs!=null?pubs:new ArrayList<Pub>();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // assumes the start and end dates are in javascript Date format
    public Race(String id, String name, ArrayList<Pub> pubs, String startDate, String endDate){
        this.id = id;
        this.name = name;
        this.pubs = pubs!=null?pubs:new ArrayList<Pub>();
        this.startDate = convertJsDate(startDate);
        this.endDate = convertJsDate(endDate);
    }

    private Date convertJsDate(String strDate){
        if(strDate!=null){
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//"yyyy-MM-dd HH:mm:ss z");
                //format.setTimeZone(TimeZone.getTimeZone("GTM+1"));
                return format.parse(strDate);
            }catch(Exception ex){
                ex.printStackTrace();
                return null;
            }
        }else return null;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void initImage(ImageView imageView){
        /*DownloadImagesTask downloadImagesTask = new DownloadImagesTask(imageView);
        downloadImagesTask.execute(this);*/
        //Drawable image = (ImageView)findViewById(R.drawable.placeholder);
    }

    public Bitmap getImage(){
        return image;
    }

    public void setPub(Pub pub, boolean inRace){
        System.out.println("DEBUG setPub");
        System.out.println(pubs!=null);
        System.out.println(inRace);
        System.out.println(pub!=null);
        if(!pubs.contains(pub) && inRace) pubs.add(pub);
        else if(pubs.contains(pub) && !inRace) pubs.remove(pub);
    }

    public ArrayList<Pub> getPubs(){
        return pubs;
    }

    public int getTagCount(){
        System.out.println("getTagCount");
        int count = 0;
        for(int i=0;i<pubs.size();i++) if(pubs.get(i).isTagged()) count++;
        return count;
    }

    public boolean isOngoing(){
        return startDate!=null && startDate.before(new Date()) && !isCompleted();
    }

    public boolean isCompleted(){
        return endDate!=null && endDate.before(new Date()) && endDate.after(startDate);
    }

    public boolean isEditable(){
        return !isOngoing() && !isCompleted();
    }

    public void setStartDate(){
        startDate = new Date();
    }

    public void setEndDate(){
        endDate = new Date();
    }

    /*public ArrayList<User> getUsers(){
        return users;
    }*/
}
