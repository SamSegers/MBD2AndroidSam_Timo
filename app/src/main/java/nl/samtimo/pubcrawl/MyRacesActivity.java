package nl.samtimo.pubcrawl;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyRacesActivity extends ColorFragmentActivity implements MyRacesListFragment.OnFragmentInteractionListener, MyRacesPubsListFragment.OnFragmentInteractionListener, MyRacesDetailFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_races);

        Button newRaceButton = (Button) findViewById(R.id.new_race_button);
        newRaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyRacesListFragment listFragment = (MyRacesListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_list);
                listFragment.addRace();
            }
        });

        Button btnSaveRace = (Button) findViewById(R.id.button_save);
        btnSaveRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyRacesDetailFragment detailFragment = (MyRacesDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_detail);
                detailFragment.saveRace();
            }
        });

        Button btnRemoveRace = (Button) findViewById(R.id.button_remove);
        btnRemoveRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyRacesDetailFragment detailFragment = (MyRacesDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_detail);
                detailFragment.removeRace();
            }
        });
    }

    @Override
    public void onPubListFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDetailFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Race race) {
        MyRacesDetailFragment fragmentDetail = (MyRacesDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_detail);
        fragmentDetail.updateDetails(race);

        MyRacesPubsListFragment fragmentPubsList = (MyRacesPubsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_my_races_pub_list);
        fragmentPubsList.update(race);
    }
}
