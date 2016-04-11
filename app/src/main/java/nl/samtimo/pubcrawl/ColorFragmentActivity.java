package nl.samtimo.pubcrawl;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by admin on 10-04-16.
 */
public class ColorFragmentActivity extends FragmentActivity implements ColorActivity{
    private String currentBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateBackgroundColor();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateBackgroundColor();
    }

    public void updateBackgroundColor(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String backgroundColor = preferences.getString("background", "#ffffff");
        if(currentBackgroundColor!=backgroundColor){
            getWindow().getDecorView().setBackgroundColor(Color.parseColor(backgroundColor));
            currentBackgroundColor = backgroundColor;
        }
    }
}
