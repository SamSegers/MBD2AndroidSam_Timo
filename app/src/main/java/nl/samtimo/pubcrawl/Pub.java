package nl.samtimo.pubcrawl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 02-04-16.
 */
public class Pub {
    private String id;
    private String name;
    private String imageURL;
    //private Bitmap image;

    public Pub(String id, String name, String imageURL){
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        System.out.println(name);
    }

    public String getid(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void initImage(ImageView imageView){
        System.out.println("download image task");
        new DownloadImageTask(imageView).execute(imageURL);
        /*try {
            InputStream inputStream = (InputStream)new URL(imageURL).getContent();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
