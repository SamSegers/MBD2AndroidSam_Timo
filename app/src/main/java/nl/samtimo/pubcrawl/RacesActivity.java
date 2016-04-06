package nl.samtimo.pubcrawl;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RacesActivity extends FragmentActivity implements RacesListFragment.OnFragmentInteractionListener, RacesDetailFragment.OnFragmentInteractionListener, RacesUsersListFragment.OnFragmentInteractionListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_races);

        Button btnJoinRace = (Button) findViewById(R.id.button_join_race);
        btnJoinRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RacesDetailFragment detailFragment = (RacesDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_races_detail);
                detailFragment.joinRace();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Race race) {
        RacesDetailFragment detailFragment = (RacesDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_races_detail);
        detailFragment.updateDetails(race);
    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
