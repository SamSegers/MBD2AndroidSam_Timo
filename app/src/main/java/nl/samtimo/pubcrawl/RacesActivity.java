package nl.samtimo.pubcrawl;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RacesActivity extends FragmentActivity implements RacesListFragment.OnFragmentInteractionListener, RacesDetailFragment.OnFragmentInteractionListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_races);

        Request request = new Request(RequestMethod.GET, "races", null, null);
        new RequestTask(this).execute(request);
    }

    public void loadRaces(String json){
        try{
            JSONArray races = new JSONArray(json);
            System.out.println(json);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onListFragmentInteraction(Object object) {

    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }
}
