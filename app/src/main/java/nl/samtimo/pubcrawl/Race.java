package nl.samtimo.pubcrawl;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 31-03-16.
 */
public class Race {
    private String id;
    private String name;
    private ArrayList<Pub> waypoints;
    //private ArrayList<User> users;
    private Date startDate;
    private Date endDate;
    private Bitmap image;

    public Race(String id, String name, ArrayList<Pub> waypoints, Date startDate, Date endDate){
        System.out.println("id: "+id);
        this.id = id;
        this.name = name;
        this.waypoints = waypoints!=null?waypoints:new ArrayList<Pub>();
        //this.users = users!=null?users:new ArrayList<User>();
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void setWaypoint(Pub pub, boolean inRace){
        if(!waypoints.contains(pub) && inRace) waypoints.add(pub);
        else if(waypoints.contains(pub) && !inRace) waypoints.remove(pub);
        System.out.println("WAYPOINTS: "+waypoints.size());
    }

    public ArrayList<Pub> getWaypoints(){
        return waypoints;
    }

    /*public ArrayList<User> getUsers(){
        return users;
    }*/
}
